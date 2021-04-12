package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.MasterConfiguration;
import com.ebizcipta.ajo.api.service.dto.MasterConfigurationDTO;
import com.ebizcipta.ajo.api.service.dto.MasterConfigurationViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface MasterConfigurationMapper {
    MasterConfigurationMapper INSTANCE = Mappers.getMapper(MasterConfigurationMapper.class);

    @Mapping(source = "entity.creationDate", target = "creationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.modificationDate", target = "modificationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.approvedDate", target = "approvedDate", qualifiedByName = "tanggalInstantToLong")
    MasterConfigurationViewDTO toDto(MasterConfiguration entity, @MappingTarget MasterConfigurationViewDTO jenisJaminanDTO);

    MasterConfiguration toEntity(MasterConfigurationDTO dto, @MappingTarget MasterConfiguration jenisJaminan);


    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }
}
