package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.AlamatBankPenerbit;
import com.ebizcipta.ajo.api.service.dto.AlamatBankPenerbitDTO;
import com.ebizcipta.ajo.api.service.dto.AlamatBankPenerbitViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface AlamatBankPenerbitMapper {
    AlamatBankPenerbitMapper INSTANCE = Mappers.getMapper(AlamatBankPenerbitMapper.class);

    AlamatBankPenerbit toEntity(AlamatBankPenerbitDTO dto, @MappingTarget AlamatBankPenerbit entity);

    @Mapping(source = "entity.creationDate", target = "creationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.modificationDate", target = "modificationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.approvedDate", target = "approvedDate", qualifiedByName = "tanggalInstantToLong")
    AlamatBankPenerbitViewDTO toDto(AlamatBankPenerbit entity, @MappingTarget AlamatBankPenerbitViewDTO dto);

    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }
}
