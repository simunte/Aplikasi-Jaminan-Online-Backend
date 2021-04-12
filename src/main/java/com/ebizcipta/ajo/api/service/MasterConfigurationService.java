package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.MasterConfigurationDTO;
import com.ebizcipta.ajo.api.service.dto.MasterConfigurationViewDTO;

import java.util.List;
import java.util.Optional;

public interface MasterConfigurationService {
    List<MasterConfigurationViewDTO> findAllMasterConfiguration(String status);
    Boolean saveMasterConfiguration(MasterConfigurationDTO masterConfigurationDTO);
    Optional<MasterConfigurationViewDTO> findMasterConfigurationById(Long id);
    Boolean approvalMasterConfiguration(GlobalApprovalDTO globalApprovalDTO);
    Boolean deleteMasterConfiguration(List<IdDTO> idDTO);
}
