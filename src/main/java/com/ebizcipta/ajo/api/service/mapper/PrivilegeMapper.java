package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.Privilege;
import com.ebizcipta.ajo.api.service.dto.PrivelegeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PrivilegeMapper extends EntityMapper<PrivelegeDTO, Privilege>{

        PrivilegeMapper INSTANCE = Mappers.getMapper(PrivilegeMapper.class);

        @Override
        @Mapping(source = "menu", target = "menu.id")
        Privilege toEntity(PrivelegeDTO dto);

        @Override
        @Mapping(source = "menu.id", target = "menu")
        PrivelegeDTO toDto(Privilege entity);
}
