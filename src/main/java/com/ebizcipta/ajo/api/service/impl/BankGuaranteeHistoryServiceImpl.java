package com.ebizcipta.ajo.api.service.impl;

import com.ebizcipta.ajo.api.domain.BankGuaranteeHistory;
import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.repositories.BankGuaranteeHistoryRepository;
import com.ebizcipta.ajo.api.repositories.RegistrationRepository;
import com.ebizcipta.ajo.api.service.BankGuaranteeHistoryService;
import com.ebizcipta.ajo.api.service.dto.BankGuaranteeHistoryDTO;
import com.ebizcipta.ajo.api.service.dto.ViewBankGuaranteeHistoryDTO;
import com.ebizcipta.ajo.api.service.mapper.BankGuaranteeHistoryMapper;
import com.ebizcipta.ajo.api.util.AuditTrailUtil;
import com.ebizcipta.ajo.api.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class BankGuaranteeHistoryServiceImpl implements BankGuaranteeHistoryService{
    @Autowired
    private BankGuaranteeHistoryRepository bankGuaranteeHistoryRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional
    public Boolean saveHistory(BankGuaranteeHistoryDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        BankGuaranteeHistory entity = new BankGuaranteeHistory();
        BankGuaranteeHistory oldEntity = null;
        Registration registration = registrationRepository.findById(dto.getIdJaminan())
                .orElseThrow(()-> new EntityNotFoundException("Nomor Jaminan Not Found"));
        Optional<BankGuaranteeHistory> bankGuaranteeHistory = bankGuaranteeHistoryRepository.findById(dto.getId());
        if (bankGuaranteeHistory.isPresent()){
            oldEntity = (BankGuaranteeHistory) SerializationUtils.clone(bankGuaranteeHistory.get());
            entity = BankGuaranteeHistoryMapper.INSTANCE.toEntity(dto, bankGuaranteeHistory.get());
        }else {
            entity = BankGuaranteeHistoryMapper.INSTANCE.toEntity(dto, entity);
        }
        entity.setRegistration(registration);

        //TODO AUDIT TRAIL SAVE HISTORY BANK GUARANTEE --DONE
        auditTrailUtil.saveAudit( bankGuaranteeHistory.isPresent() ? Constants.Event.UPDATE : Constants.Event.CREATE , Constants.Module.BANK_GUARANTEE_HISTORY , bankGuaranteeHistory.isPresent() ? new JSONObject(oldEntity).toString() : null, new JSONObject(entity).toString(),  bankGuaranteeHistory.isPresent() ? Constants.Remark.UPDATE_BG_HISTORY : Constants.Remark.CREATE_BG_HISTORY,userLogin );

        bankGuaranteeHistoryRepository.save(entity);

        return Boolean.TRUE;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewBankGuaranteeHistoryDTO> findAllHistory() {
        return bankGuaranteeHistoryRepository.findAll().stream()
                .map(bankGuaranteeHistory -> BankGuaranteeHistoryMapper.INSTANCE.toDTO(bankGuaranteeHistory, new ViewBankGuaranteeHistoryDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ViewBankGuaranteeHistoryDTO> findByNomorJaminan(Long idJaminan) {
        Registration registration = registrationRepository.findById(idJaminan)
                .orElseThrow(()-> new EntityNotFoundException("Nomor Jaminan Tidak Ditemukan"));
        return bankGuaranteeHistoryRepository.findByRegistrationOrderByCreationDateAsc(registration).stream()
                .map(bankGuaranteeHistory -> BankGuaranteeHistoryMapper.INSTANCE.toDTO(bankGuaranteeHistory, new ViewBankGuaranteeHistoryDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }


}
