package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.exception.BadRequestAlertException;
import com.ebizcipta.ajo.api.repositories.RegistrationRepository;
import com.ebizcipta.ajo.api.service.ManualDataTransferService;
import com.ebizcipta.ajo.api.service.dto.BankGuaranteeHistoryDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.ViewRegistrationDTO;
import com.ebizcipta.ajo.api.service.mapper.ViewRegistrationMapper;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import com.ebizcipta.ajo.api.util.Statusutil;
import com.ebizcipta.ajo.api.util.WebServicesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ManualDataTransferServiceImpl implements ManualDataTransferService {
    private final RegistrationRepository registrationRepository;
    private final BankGuaranteeHistoryServiceImpl bankGuaranteeHistoryService;
    private final WebServicesUtil webServicesUtil;

    @Autowired
    private AuditTrailUtil auditTrailUtil;

    public ManualDataTransferServiceImpl(RegistrationRepository registrationRepository, BankGuaranteeHistoryServiceImpl bankGuaranteeHistoryService, WebServicesUtil webServicesUtil) {
        this.registrationRepository = registrationRepository;
        this.bankGuaranteeHistoryService = bankGuaranteeHistoryService;
        this.webServicesUtil = webServicesUtil;
    }

    @Override
    public List<ViewRegistrationDTO> findAllBgTransfer() {
        return registrationRepository.findByBgStatusIn(Statusutil.getStatusManualDataTransfer())
                .stream()
                .map(registration -> ViewRegistrationMapper.INSTANCE.toDto(registration, new ViewRegistrationDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public Boolean sendManualBg(List<IdDTO> idDTOList) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        idDTOList.forEach(idDTO -> {
            Registration registration = registrationRepository.findById(idDTO.getId())
                    .orElseThrow(()-> new BadRequestAlertException("Bank Guarantee Tidak Ditemukan", "", ""));
            if (registration.getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.APPROVEDBG)
                    || registration.getBgStatus().equalsIgnoreCase(Constants.BankGuaranteeStatus.FAILED_BG)){
                Boolean hitAbg = Boolean.FALSE;
                try {
                    hitAbg = webServicesUtil.PlnAbgApi(registration);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Registration oldRegistration = (Registration) SerializationUtils.clone(registration);
                if (hitAbg){

                    registration.setBgStatus(Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION);
                    registrationRepository.save(registration);
                    //TODO AUDIT TRAIL BG REGISTRATION SET STATUS WAITING VERIFICATION
                    auditTrailUtil.saveAudit(
                            Constants.Event.UPDATE ,
                            Constants.Module.REGISTRATION,
                            new JSONObject(oldRegistration).toString(),
                            new JSONObject(registration).toString(),
                            Constants.Remark.UPDATE_REGISTRATION_BG_WAITING_VERIFICATION_MANUAL_DATA_TRANSFER ,
                            userLogin );

                    //SAVE HISTORY
                    BankGuaranteeHistoryDTO dtoHistory = new BankGuaranteeHistoryDTO();
                    dtoHistory.setId(0L);
                    dtoHistory.setIdJaminan(registration.getId());
                    dtoHistory.setBgStatus(Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION);
                    dtoHistory.setNotes("");
                    bankGuaranteeHistoryService.saveHistory(dtoHistory);

                    //TODO AUDIT TRAIL CREATE BG HISTORY FROM MANUAL DATA TRANSFER
                    auditTrailUtil.saveAudit(
                            Constants.Event.CREATE ,
                            Constants.Module.BANK_GUARANTEE_HISTORY ,
                            null,
                            new JSONObject(dtoHistory).toString(),
                            Constants.Remark.CREATE_BG_HISTORY_MANUAL_DATA_TRANSFER_WAITING,
                            userLogin );
                }else {
                    registration.setBgStatus(Constants.BankGuaranteeStatus.FAILED_BG);
                    registrationRepository.save(registration);
                    //TODO AUDIT TRAIL BG REGISTRATION SET STATUS FAILED
                    auditTrailUtil.saveAudit(
                            Constants.Event.UPDATE ,
                            Constants.Module.REGISTRATION,
                            new JSONObject(oldRegistration).toString(),
                            new JSONObject(registration).toString(),
                            Constants.Remark.UPDATE_REGISTRATION_BG_FAILED_MANUAL_DATA_TRANSFER ,
                            userLogin );
                    //SAVE HISTORY
                    BankGuaranteeHistoryDTO dtoHistory = new BankGuaranteeHistoryDTO();
                    dtoHistory.setId(0L);
                    dtoHistory.setIdJaminan(registration.getId());
                    dtoHistory.setBgStatus(Constants.BankGuaranteeStatus.FAILED_BG);
                    dtoHistory.setNotes("");
                    bankGuaranteeHistoryService.saveHistory(dtoHistory);
                    //TODO AUDIT TRAIL CREATE BG HISTORY FROM MANUAL DATA TRANSFER
                    auditTrailUtil.saveAudit(
                            Constants.Event.CREATE ,
                            Constants.Module.BANK_GUARANTEE_HISTORY ,
                            null,
                            new JSONObject(dtoHistory).toString(),
                            Constants.Remark.CREATE_BG_HISTORY_MANUAL_DATA_TRANSFER_FAILED,
                            userLogin );
                }
            }else {
                throw new BadRequestAlertException("Status Tidak Valid","","");
            }
        });
        return Boolean.TRUE;
    }
}
