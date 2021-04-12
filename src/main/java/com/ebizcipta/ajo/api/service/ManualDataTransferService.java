package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.IdDTO;
import com.ebizcipta.ajo.api.service.dto.ViewRegistrationDTO;

import java.util.List;

public interface ManualDataTransferService {
    List<ViewRegistrationDTO> findAllBgTransfer();
    Boolean sendManualBg(List<IdDTO> idDTOList);
}
