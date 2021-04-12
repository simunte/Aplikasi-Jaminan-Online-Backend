package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.Confirmation;
import com.ebizcipta.ajo.api.service.dto.ConfirmationDTO;
import com.ebizcipta.ajo.api.service.dto.ViewConfirmationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface ConfirmationMapper {
    ConfirmationMapper INSTANCE = Mappers.getMapper(ConfirmationMapper.class);

    @Mapping(source = "dto.tanggalSuratKonfirmasi", target = "tanggalSuratKonfirmasi", qualifiedByName = "tanggalLongToInstant")
    Confirmation toEntity(ConfirmationDTO dto, @MappingTarget Confirmation entity);

    @Mapping(source = "entity.tanggalSuratKonfirmasi", target = "tanggalSuratKonfirmasi", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity", target = "penandaTangan", qualifiedByName = "penandaTangan")
    @Mapping(source = "user.position", target = "jabatanPenandaTangan")
    @Mapping(source = "registration.nomorJaminan", target = "nomorJaminan")
    ViewConfirmationDTO toDto(Confirmation entity, @MappingTarget ViewConfirmationDTO dto);

    @Named("tanggalLongToInstant")
    default Instant tanggalLongToInstant(Long tanggalLong){
        return Instant.ofEpochMilli(tanggalLong);
    }

    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant){
        return tanggalInstant.toEpochMilli();
    }

    @Named("penandaTangan")
    default String penandaTangan(Confirmation entity){
        String fullname;
        if (entity.getUser().getLastName()!=null){
            fullname = entity.getUser().getFirstName()+" "+entity.getUser().getLastName();
        }else {
            fullname = entity.getUser().getFirstName();
        }
        return fullname;
    }
}
