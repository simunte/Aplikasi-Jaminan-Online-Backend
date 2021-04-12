package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.service.dto.RegistrationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface RegistrationMapper{
    RegistrationMapper INSTANCE = Mappers.getMapper(RegistrationMapper.class);

    Registration toEntity(RegistrationDTO dto, @MappingTarget Registration entity);
    RegistrationDTO toDto(Registration entity, @MappingTarget RegistrationDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "creationDate", ignore = true)
    @Mapping(target = "modificationDate", ignore = true)
    @Mapping(target = "approvedDate", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "modifiedBy", ignore = true)
    @Mapping(target = "approvedBy", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "notes", ignore = true)
    @Mapping(target = "userApprove", ignore = true)
    Registration oldToNewEntity(Registration oldEntity, @MappingTarget Registration newEntity);

    @Named("tanggalLongToInstant")
    default Instant tanggalLongToInstant(Long tanggalLong) {
        return Instant.ofEpochMilli(tanggalLong);
    }
}
