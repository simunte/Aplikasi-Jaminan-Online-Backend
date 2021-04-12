package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.JenisProduk;
import com.ebizcipta.ajo.api.service.dto.JenisJaminanDTO;
import com.ebizcipta.ajo.api.service.dto.JenisProdukDTO;
import com.ebizcipta.ajo.api.service.dto.JenisProdukViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface JenisProdukMapper {
    JenisProdukMapper INSTANCE = Mappers.getMapper(JenisProdukMapper.class);

    @Mapping(source = "entity.creationDate", target = "creationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.modificationDate", target = "modificationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.approvedDate", target = "approvedDate", qualifiedByName = "tanggalInstantToLong")
    JenisProdukViewDTO toDto(JenisProduk entity, @MappingTarget JenisProdukViewDTO jenisProdukDTO);

    JenisProduk toEntity(JenisProdukDTO dto, @MappingTarget JenisProduk jenisProduk);

    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }
}
