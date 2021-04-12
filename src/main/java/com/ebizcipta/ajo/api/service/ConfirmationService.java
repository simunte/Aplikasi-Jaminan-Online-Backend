package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.ConfirmationDTO;
import com.ebizcipta.ajo.api.service.dto.ViewConfirmationDTO;

import java.util.List;
import java.util.Optional;

public interface ConfirmationService {
    List<ViewConfirmationDTO> findAllConfirmation();
    Optional<ViewConfirmationDTO> findConfirmationByNomorJaminan(Long idJaminan);
    Boolean saveConfirmation(ConfirmationDTO confirmationDTO, String draft);
}
