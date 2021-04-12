package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.service.dto.BankGuaranteeHistoryDTO;
import com.ebizcipta.ajo.api.service.dto.ViewBankGuaranteeHistoryDTO;

import java.util.List;

public interface BankGuaranteeHistoryService {
    Boolean saveHistory(BankGuaranteeHistoryDTO dto);
    List<ViewBankGuaranteeHistoryDTO> findAllHistory();
    List<ViewBankGuaranteeHistoryDTO> findByNomorJaminan(Long idJaminan);
}
