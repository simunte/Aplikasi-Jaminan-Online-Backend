package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.JenisJaminan;
import com.ebizcipta.ajo.api.service.dto.JenisJaminanDTO;
import com.ebizcipta.ajo.api.service.dto.JenisJaminanViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface JenisJaminanMapper {
    JenisJaminanMapper INSTANCE = Mappers.getMapper(JenisJaminanMapper.class);


    @Mapping(source = "entity.creationDate", target = "creationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.modificationDate", target = "modificationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.approvedDate", target = "approvedDate", qualifiedByName = "tanggalInstantToLong")
    JenisJaminanViewDTO toDto(JenisJaminan entity, @MappingTarget JenisJaminanViewDTO jenisJaminanDTO);

    JenisJaminan toEntity(JenisJaminanDTO dto, @MappingTarget JenisJaminan jenisJaminan);


    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }
}
