package com.ebizcipta.ajo.api.service.mapper;


import com.ebizcipta.ajo.api.domain.Role;
import com.ebizcipta.ajo.api.service.dto.RoleListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface RoleListMapper {

    RoleListMapper INSTANCE = Mappers.getMapper(RoleListMapper.class);

    @Mapping(source = "entity.modificationDate", target = "modifiedDate", qualifiedByName = "tanggalInstantToLong")
    RoleListDTO toDto(Role entity, @MappingTarget RoleListDTO dto);

    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }
}
