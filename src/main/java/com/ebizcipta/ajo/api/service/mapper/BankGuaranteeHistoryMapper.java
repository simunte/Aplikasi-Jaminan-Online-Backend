package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.BankGuaranteeHistory;
import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.service.dto.BankGuaranteeHistoryDTO;
import com.ebizcipta.ajo.api.service.dto.ViewBankGuaranteeHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface BankGuaranteeHistoryMapper {
    BankGuaranteeHistoryMapper INSTANCE = Mappers.getMapper(BankGuaranteeHistoryMapper.class);

    BankGuaranteeHistory toEntity(BankGuaranteeHistoryDTO dto, @MappingTarget BankGuaranteeHistory entity);

    @Mapping(source = "registration.nomorJaminan", target = "nomorJaminan")
    @Mapping(source = "entity.creationDate", target = "creationDate", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity.modificationDate", target = "modificationDate", qualifiedByName = "tanggalInstantToLong")
    ViewBankGuaranteeHistoryDTO toDTO (BankGuaranteeHistory entity, @MappingTarget ViewBankGuaranteeHistoryDTO dto);

    BankGuaranteeHistoryDTO RegistrationToDTO(Registration entity, @MappingTarget BankGuaranteeHistoryDTO dto);

    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }
}
