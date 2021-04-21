package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.NasabahSupportDocument;
import com.ebizcipta.ajo.api.service.dto.SupportDocumentNasabahDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NasabahSupportDocumentMapper {
    NasabahSupportDocumentMapper INSTANCE = Mappers.getMapper(NasabahSupportDocumentMapper.class);

    SupportDocumentNasabahDTO toDto(NasabahSupportDocument entity, @MappingTarget SupportDocumentNasabahDTO dto);
    NasabahSupportDocument toEntity(SupportDocumentNasabahDTO dto, @MappingTarget NasabahSupportDocument entity);
}
