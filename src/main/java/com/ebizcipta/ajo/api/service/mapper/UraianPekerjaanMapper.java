package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.UraianPekerjaan;
import com.ebizcipta.ajo.api.service.dto.UraianPekerjaanDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UraianPekerjaanMapper {
    UraianPekerjaanMapper INSTANCE = Mappers.getMapper(UraianPekerjaanMapper.class);

    UraianPekerjaanDTO toDto(UraianPekerjaan entity, @MappingTarget UraianPekerjaanDTO uraianPekerjaanDTO);

    UraianPekerjaan toEntity(UraianPekerjaanDTO dto, @MappingTarget UraianPekerjaan uraianPekerjaan);
}
