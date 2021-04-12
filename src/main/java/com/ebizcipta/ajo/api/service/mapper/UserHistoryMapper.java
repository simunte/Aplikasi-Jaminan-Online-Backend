package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.UraianPekerjaan;
import com.ebizcipta.ajo.api.domain.UserHistory;
import com.ebizcipta.ajo.api.service.dto.UraianPekerjaanDTO;
import com.ebizcipta.ajo.api.service.dto.UserHistoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface UserHistoryMapper {
    UserHistoryMapper INSTANCE = Mappers.getMapper(UserHistoryMapper.class);

    @Mapping(source = "entity.creationDate", target = "createdDate", qualifiedByName = "tanggalInstantToLong")
    UserHistoryDTO toDto(UserHistory entity, @MappingTarget UserHistoryDTO userHistoryDTO);


    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }
}