package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.WebServicesAbgDTO;
import com.ebizcipta.ajo.api.service.dto.WebServicesAbgViewDTO;

import java.util.List;
import java.util.Optional;

public interface WebServicesAbgService {
    List<WebServicesAbgViewDTO> findAllWebServicesAbg(String status);
    Boolean saveWebServicesAbg(WebServicesAbgDTO dto);
    Optional<WebServicesAbgViewDTO> findWebServicesAbgById(Long id);
    Boolean approvalWebServicesAbg(GlobalApprovalDTO globalApprovalDTO);
    Boolean deleteWebServicesAbg(List<IdDTO> idDTO);
}
