package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.Registration;
import com.ebizcipta.ajo.api.service.dto.BankGuaranteeDTO;
import com.ebizcipta.ajo.api.service.dto.ViewConfirmationDTO;
import com.ebizcipta.ajo.api.service.dto.ViewRegistrationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Duration;
import java.time.Instant;

@Mapper(componentModel = "spring")
public interface ViewRegistrationMapper{
    ViewRegistrationMapper INSTANCE = Mappers.getMapper(ViewRegistrationMapper.class);

    @Mapping(source = "jenisProduk.jenisProduk", target = "jenisProduk")
    @Mapping(source = "jenisJaminan.jenisJaminan", target = "jenisJaminan")
    @Mapping(source = "beneficiary.namaBeneficiary", target = "beneficiary")
    @Mapping(source = "unitPengguna.unitPengguna", target = "unitPengguna")
    @Mapping(source = "currency.currency", target = "currency")
    @Mapping(source = "alamatBankPenerbit.alamatBankPenerbit", target = "alamatBankPenerbit")
//    @Mapping(source = "entity.tanggalTerbit", target = "tanggalTerbit", qualifiedByName = "tanggalInstantToLong")
//    @Mapping(source = "entity.tanggalBerlaku", target = "tanggalBerlaku", qualifiedByName = "tanggalInstantToLong")
//    @Mapping(source = "entity.tanggalBerakhir", target = "tanggalBerakhir", qualifiedByName = "tanggalInstantToLong")
//    @Mapping(source = "entity.tanggalBatasClaim", target = "tanggalBatasClaim", qualifiedByName = "tanggalInstantToLong")
    @Mapping(source = "entity", target = "rowColor", qualifiedByName = "colorOfRow")
    ViewRegistrationDTO toDto(Registration entity, @MappingTarget ViewRegistrationDTO dto);

    ViewRegistrationDTO registrationToBankGuaranteeDTO(ViewRegistrationDTO viewRegistrationDTO, @MappingTarget ViewRegistrationDTO dto);

    ViewConfirmationDTO confirmationToBankGuaranteeDTO(ViewConfirmationDTO viewConfirmationDTO, @MappingTarget ViewConfirmationDTO dto);

    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }

    @Named("colorOfRow")
    default String colorOfRow(Registration registration){
        String rowColor = null;
        Instant toDay = Instant.now();
        Duration toDayTglBerakhir = Duration.between(toDay, toDay);
        Duration toDayTglApprove = Duration.between(toDay, toDay);
        Instant tglBerakhir = Instant.ofEpochMilli(registration.getTanggalBerakhir());
        if (registration.getTanggalBerakhir() != null){
            toDayTglBerakhir = Duration.between(tglBerakhir, toDay);
        }
        if (registration.getApprovedDate() != null){
            toDayTglApprove = Duration.between(toDay, registration.getApprovedDate());
        }
        if (toDayTglBerakhir.abs().toDays() < 8 && toDayTglBerakhir.abs().toDays() >= 0){
            rowColor = "red";
        }else if (toDayTglBerakhir.abs().toDays() < 31 && toDayTglBerakhir.abs().toDays() > 7){
            rowColor = "yellow";
        }else if (toDayTglApprove.abs().toDays() < 7 && toDayTglApprove.abs().toDays() >= 0){
            rowColor = "green";
        }else {
            rowColor = "white";
        }
        return rowColor;
    }
}
