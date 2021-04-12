package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.UnitPengguna;
import com.ebizcipta.ajo.api.service.dto.UnitPenggunaDTO;
import com.ebizcipta.ajo.api.service.dto.UnitPenggunaVIewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface UnitPenggunaMapper{
    UnitPenggunaMapper INSTANCE = Mappers.getMapper(UnitPenggunaMapper.class);

    UnitPengguna toEntity(UnitPenggunaDTO dto, @MappingTarget UnitPengguna unitPengguna);

    @Mapping(source = "entity.creationDate", target = "creationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.modificationDate", target = "modificationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.approvedDate", target = "approvedDate", qualifiedByName = "tanggalInstantToLong")
    UnitPenggunaVIewDTO toDto(UnitPengguna entity, @MappingTarget UnitPenggunaVIewDTO unitPenggunaDTO);

    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }
}
