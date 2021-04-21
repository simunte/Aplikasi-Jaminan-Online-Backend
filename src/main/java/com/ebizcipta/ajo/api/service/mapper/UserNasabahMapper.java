package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.UserNasabah;
import com.ebizcipta.ajo.api.service.dto.UserNasabahDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface UserNasabahMapper {
    UserNasabahMapper INSTANCE = Mappers.getMapper(UserNasabahMapper.class);

    @Mapping(source = "entity.creationDate", target = "creationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.modificationDate", target = "modificationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.approvedDate", target = "approvedDate", qualifiedByName = "tanggalInstantToLong")
    UserNasabahDTO toDto(UserNasabah entity, @MappingTarget UserNasabahDTO dto);

    @Mapping(source = "dto.creationDate", target = "creationDate", qualifiedByName = "tanggalLongTOIstant")
    @Mapping(source = "dto.modificationDate", target = "modificationDate", qualifiedByName = "tanggalLongTOIstant")
    @Mapping(source = "dto.approvedDate", target = "approvedDate", qualifiedByName = "tanggalLongTOIstant")
    UserNasabah toEntity(UserNasabahDTO dto, @MappingTarget UserNasabah entity);

    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }

    @Named("tanggalLongTOIstant")
    default Instant tanggalInstantToLong(Long tanggal) {
        if (tanggal == null){
            return null;
        }else {
            return Instant.ofEpochMilli(tanggal);
        }
    }
}
