package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.CurrencyDTO;
import com.ebizcipta.ajo.api.service.dto.CurrencyViewDTO;
import com.ebizcipta.ajo.api.service.dto.GlobalApprovalDTO;
import com.ebizcipta.ajo.api.service.dto.IdDTO;

import java.util.List;
import java.util.Optional;

public interface CurrencyService {
    List<CurrencyViewDTO> findAllCurrency(String status);
    Boolean saveCurrency(CurrencyDTO currencyDTO);
    Optional<CurrencyViewDTO> findCurrencyById(Long id);
    Boolean approvalCurrency(GlobalApprovalDTO globalApprovalDTO);
    Boolean deleteCurrency(List<IdDTO> idDTO);
}
