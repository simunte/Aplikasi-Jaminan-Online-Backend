package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.BeneficiaryDTO;
import com.ebizcipta.ajo.api.service.dto.BeneficiaryViewDTO;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;

import java.util.List;
import java.util.Optional;

public interface BeneficiaryService {
    List<BeneficiaryViewDTO> findAllBeneficiary(String status);
    Boolean saveBeneficiary(BeneficiaryDTO beneficiaryDTO);
    Optional<BeneficiaryViewDTO> findBeneficiaryById(Long id);
    Boolean approvalBeneficiary(GlobalApprovalDTO globalApprovalDTO);
    Boolean deleteBeneficiary(List<IdDTO> idDTO);
}
