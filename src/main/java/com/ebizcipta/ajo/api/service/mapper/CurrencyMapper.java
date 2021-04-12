package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.Currency;
import com.ebizcipta.ajo.api.service.dto.CurrencyDTO;
import com.ebizcipta.ajo.api.service.dto.CurrencyViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mapping(source = "entity.creationDate", target = "creationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.modificationDate", target = "modificationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.approvedDate", target = "approvedDate", qualifiedByName = "tanggalInstantToLong")
    CurrencyViewDTO toDto(Currency entity, @MappingTarget CurrencyViewDTO dto);

    Currency toEntity(CurrencyDTO dto, @MappingTarget Currency currency);

    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }
}
