package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.*;

import java.util.List;
import java.util.Optional;

public interface JenisJaminanService {
    List<JenisJaminanViewDTO> findAllJenisJaminan(String status);
    Boolean saveJenisJaminan(JenisJaminanDTO jenisJaminanDTO);
    Optional<JenisJaminanViewDTO> findJenisJaminanById(Long id);
    Boolean approvalJenisJaminan(GlobalApprovalDTO globalApprovalDTO);
    Boolean deleteJenisJaminan(List<IdDTO> idDTO);
}
