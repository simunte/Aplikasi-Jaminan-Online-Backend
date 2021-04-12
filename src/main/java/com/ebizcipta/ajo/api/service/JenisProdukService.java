package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public interface JenisProdukService {
    List<JenisProdukViewDTO> findAllJenisProduk(String status);
    Boolean saveJenisProduk(JenisProdukDTO jenisProdukDTO);
    Optional<JenisProdukViewDTO> findJenisProdukById(Long id);
    Boolean approvalJenisProduk(GlobalApprovalDTO globalApprovalDTO);
    Boolean deleteJenisProduk(List<IdDTO> idDTO);
}
