package com.ebizcipta.ajo.api.service.impl;


import com.ebizcipta.ajo.api.domain.Confirmation;
import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.repositories.ConfirmationRepository;
import com.ebizcipta.ajo.api.repositories.RegistrationRepository;
import com.ebizcipta.ajo.api.repositories.UserRepository;
import com.ebizcipta.ajo.api.service.ConfirmationService;
import com.ebizcipta.ajo.api.service.dto.BankGuaranteeHistoryDTO;
import com.ebizcipta.ajo.api.service.dto.ConfirmationDTO;
import com.ebizcipta.ajo.api.service.dto.ViewConfirmationDTO;
import com.ebizcipta.ajo.api.service.mapper.ConfirmationMapper;
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
import java.io.File;
import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ConfirmationServiceImpl implements ConfirmationService{

    @Autowired
    ConfirmationRepository confirmationRepository;
    @Autowired
    RegistrationRepository registrationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    BankGuaranteeHistoryServiceImpl bankGuaranteeHistoryService;
    @Autowired
    private FileManagementServiceImpl fileManagementService;
    @Autowired
    private AuditTrailUtil auditTrailUtil;

    @Override
    @Transactional(readOnly = true)
    public List<ViewConfirmationDTO> findAllConfirmation() {
        return confirmationRepository.findAll().stream()
                .map(confirmation -> ConfirmationMapper.INSTANCE.toDto(confirmation, new ViewConfirmationDTO()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ViewConfirmationDTO> findConfirmationByNomorJaminan(Long idJaminan) {
        Registration registration = registrationRepository.findById(idJaminan)
                .orElseThrow(()->new EntityNotFoundException("Nomor Jaminan Not Found"));
        return confirmationRepository.findByRegistration(registration)
                .map(confirmation -> ConfirmationMapper.INSTANCE.toDto(confirmation, new ViewConfirmationDTO()));
    }

    @Override
    @Transactional
    public Boolean saveConfirmation(ConfirmationDTO confirmationDTO, String draft) {
        Confirmation confirmation = new Confirmation();
        String status;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();

        Registration registration= registrationRepository.findById(confirmationDTO.getIdJaminan())
                .orElseThrow(()->new EntityNotFoundException("Nomor Jaminan Not Found"));
        User user = userRepository.findOneByUsernameAndIsEnabled(confirmationDTO.getUsername(), true)
                .orElseThrow(()->new EntityNotFoundException("User Not Found"));
        Optional<Confirmation> confirmationFound = confirmationRepository.findByNomorKonfirmasiBankAndRegistration(confirmationDTO.getNomorKonfirmasiBank(), registration);

        Confirmation oldConfirmation = null;
        Registration oldRegistration= (Registration) SerializationUtils.clone(registration);

        if (!confirmationFound.isPresent()){
            confirmation = ConfirmationMapper.INSTANCE.toEntity(confirmationDTO, confirmation);
            if (Boolean.parseBoolean(draft)){
                status = Constants.BankGuaranteeStatus.DRAFT;
            }else {
                status = Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL;
            }
        }else {
            oldConfirmation = (Confirmation) SerializationUtils.clone(confirmationFound.get());
            if (Boolean.parseBoolean(draft)){
                status = Constants.BankGuaranteeStatus.DRAFT;
            }else {
                status = Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL;
            }
            confirmation = ConfirmationMapper.INSTANCE.toEntity(confirmationDTO, confirmationFound.get());
        }
        confirmation.setRegistration(registration);
        confirmation.setUser(user);
        confirmationRepository.save(confirmation);

        //TODO AUDIT TRAIL SAVE CONFIRMATION
        auditTrailUtil.saveAudit(
                confirmationFound.isPresent() ? Constants.Event.UPDATE : Constants.Event.CREATE,
                Constants.Module.CONFIRMATION,
                confirmationFound.isPresent() ? new JSONObject(oldConfirmation).toString() : null,
                new JSONObject(confirmation).toString(),
                Constants.Remark.CREATE_CONFIRMATION_BG,
                userLogin );

        registration.setBgStatus(status);

        //TODO AUDIT TRAIL SAVE REGISTRATION SET STATUS BG WAITING APPROVAL OR DRAFT
        auditTrailUtil.saveAudit(
                Constants.Event.UPDATE,
                Constants.Module.REGISTRATION,
                new JSONObject(oldRegistration).toString(),
                new JSONObject(registration).toString(),
                status.equalsIgnoreCase(Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL) ? Constants.Remark.UPDATE_REGISTRATION_BG_STATUS_WAITING_APPROVAL : Constants.Remark.UPDATE_REGISTRATION_BG_STATUS_DRAFT,
                userLogin );

        return Boolean.TRUE;
    }
}
