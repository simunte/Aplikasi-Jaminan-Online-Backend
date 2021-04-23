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


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userLogin= authentication.getName();
        User userPresent = userRepository.findOneByUsernameAndIsEnabled(authentication.getName(), true)
                .orElseThrow(()->new EntityNotFoundException("User Not Found"));

        Registration registration= registrationRepository.findById(confirmationDTO.getIdJaminan())
                .orElseThrow(()->new EntityNotFoundException("Nomor Jaminan Not Found"));
        String status = registration.getBgStatus();
        User user = userRepository.findOneByUsernameAndIsEnabled(confirmationDTO.getUsername(), true)
                .orElseThrow(()->new EntityNotFoundException("User Not Found"));
        Optional<Confirmation> confirmationFound = confirmationRepository.findByNomorKonfirmasiBankAndRegistration(confirmationDTO.getNomorKonfirmasiBank(), registration);

        Confirmation oldConfirmation = null;
        Registration oldRegistration= (Registration) SerializationUtils.clone(registration);

        if (!confirmationFound.isPresent()){
            confirmation = ConfirmationMapper.INSTANCE.toEntity(confirmationDTO, confirmation);
            if (Boolean.parseBoolean(draft)){
                status = Constants.BankGuaranteeStatus.DRAFT;
            }
//            else {
//                if(userPresent.getRoles().get(0).getCode().equalsIgnoreCase(Constants.Role.NASABAH)){
//                    status = Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL;
//                }else if(userPresent.getRoles().get(0).getCode().equalsIgnoreCase(Constants.Role.TRO_MAKER)){
//                    status = Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION;
//                }else if(userPresent.getRoles().get(0).getCode().equalsIgnoreCase(Constants.Role.TRO_CHECKER)){
//                    status = Constants.BankGuaranteeStatus.WAITINGBGVALIDATION;
//                }else if(userPresent.getRoles().get(0).getCode().equalsIgnoreCase(Constants.Role.BENEFICIARY_USER)){
//                    status = Constants.BankGuaranteeStatus.APPROVEDBG;
//                }else{
//                    status = Constants.BankGuaranteeStatus.WAITINGBGCONFIRMATION;
//                }
//            }
        }else {
            oldConfirmation = (Confirmation) SerializationUtils.clone(confirmationFound.get());
            if (Boolean.parseBoolean(draft)){
                status = Constants.BankGuaranteeStatus.DRAFT;
            }
//            else {
//                if(userPresent.getRoles().get(0).getCode().equalsIgnoreCase(Constants.Role.NASABAH)){
//                    status = Constants.BankGuaranteeStatus.WAITINGBGAPPROVAL;
//                }else if(userPresent.getRoles().get(0).getCode().equalsIgnoreCase(Constants.Role.TRO_MAKER)){
//                    status = Constants.BankGuaranteeStatus.WAITINGBGVERIFICATION;
//                }else if(userPresent.getRoles().get(0).getCode().equalsIgnoreCase(Constants.Role.TRO_CHECKER)){
//                    status = Constants.BankGuaranteeStatus.WAITINGBGVALIDATION;
//                }else if(userPresent.getRoles().get(0).getCode().equalsIgnoreCase(Constants.Role.BENEFICIARY_USER)){
//                    status = Constants.BankGuaranteeStatus.APPROVEDBG;
//                }else{
//                    status = Constants.BankGuaranteeStatus.WAITINGBGCONFIRMATION;
//                }
//
//            }
            confirmation = ConfirmationMapper.INSTANCE.toEntity(confirmationDTO, confirmationFound.get());
        }
        confirmation.setRegistration(registration);
        confirmation.setUser(user);
        confirmationRepository.save(confirmation);

        registration.setBgStatus(status);

        return Boolean.TRUE;
    }
}
