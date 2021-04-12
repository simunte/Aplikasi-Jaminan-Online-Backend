package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.WebServicesAbg;
import com.ebizcipta.ajo.api.service.dto.WebServicesAbgDTO;
import com.ebizcipta.ajo.api.service.dto.WebServicesAbgViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface WebServicesAbgMapper {

    WebServicesAbgMapper INSTANCE = Mappers.getMapper(WebServicesAbgMapper.class);

    WebServicesAbg toEntity(WebServicesAbgDTO dto, @MappingTarget WebServicesAbg entity);

    @Mapping(source = "entity.creationDate", target = "creationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.modificationDate", target = "modificationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.approvedDate", target = "approvedDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "beneficiary.namaBeneficiary", target = "beneficiaryId")
    WebServicesAbgViewDTO toDTO(WebServicesAbg entity, @MappingTarget WebServicesAbgViewDTO dto);
    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }
}
