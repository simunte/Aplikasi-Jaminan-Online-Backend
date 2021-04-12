package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.*;

import java.util.List;
import java.util.Optional;

public interface UnitPenggunaService {
    List<UnitPenggunaVIewDTO> findAllUnitPengguna(String status);
    Boolean saveUnitPengguna(UnitPenggunaDTO unitPenggunaDTO);
    Optional<UnitPenggunaVIewDTO> findJUnitPenggunaById(Long id);
    Boolean approvalUnitPengguna(GlobalApprovalDTO globalApprovalDTO);
    Boolean deleteUnitPengguna(List<IdDTO> idDTO);
}
