package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.*;

import java.util.List;
import java.util.Optional;

public interface RegistrationService {
    List<ViewRegistrationDTO> findAllBgRegistration(String status);
    List<ViewRegistrationDTO> findAllBgPLN(String status, String menu);
    Optional<ViewRegistrationDTO> findByNomorJaminan(Long idJaminan);
    ConfirmationDTO saveRegistration(RegistrationDTO dto);
    Boolean registrationApproval(ApprovalDTO approvalDTO);
    Optional<BankGuaranteeDTO> getDetailBankGuarantee(Long id_jaminan);
    Boolean submitDraft(Long idJaminan, Boolean draft);
    Boolean deleteBankGuarantee(List <IdDTO> idDTOS);
}
