package com.ebizcipta.ajo.api.service;

import com.ebizcipta.ajo.api.service.dto.ColumnUserDTO;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

public interface ColumnUserService {
    Boolean saveColumnUser(ColumnUserDTO columnUserDTO);
    List<ColumnUserDTO> findAllColumnUser();
    Optional<ColumnUserDTO> findByUsername();
}
