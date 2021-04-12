package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.Beneficiary;
import com.ebizcipta.ajo.api.service.dto.BeneficiaryDTO;
import com.ebizcipta.ajo.api.service.dto.BeneficiaryViewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface BeneficiaryMapper {
    BeneficiaryMapper INSTANCE = Mappers.getMapper(BeneficiaryMapper.class);

    Beneficiary toEntity(BeneficiaryDTO dto, @MappingTarget Beneficiary entity);

    @Mapping(source = "entity.creationDate", target = "creationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.modificationDate", target = "modificationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.approvedDate", target = "approvedDate", qualifiedByName = "tanggalInstantToLong")
    BeneficiaryViewDTO toDto(Beneficiary entity, @MappingTarget BeneficiaryViewDTO dto);
    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }
}
