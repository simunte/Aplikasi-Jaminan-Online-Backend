package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.AlamatBankPenerbitDTO;
import com.ebizcipta.ajo.api.service.dto.AlamatBankPenerbitViewDTO;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;

import java.util.List;
import java.util.Optional;

public interface AlamatBankPenerbitService {
    List<AlamatBankPenerbitViewDTO> findAllAlamatBankPenerbit(String status);
    Boolean saveAlamatBankPenerbit(AlamatBankPenerbitDTO dto);
    Optional<AlamatBankPenerbitViewDTO> findAlamatBankPenerbitById(Long id);
    Boolean approvalAlamatBankPenerbit(GlobalApprovalDTO globalApprovalDTO);
    Boolean deleteAlamatBankPenerbit(List<IdDTO> idDTO);
}
