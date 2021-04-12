package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.ColumnUser;
import com.ebizcipta.ajo.api.service.dto.ColumnUserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ColumnUserMapper {
    ColumnUserMapper INSTANCE = Mappers.getMapper(ColumnUserMapper.class);


    ColumnUser toEntity(ColumnUserDTO dto, @MappingTarget ColumnUser entity);

    @Mapping(source = "user.username", target = "username")
    ColumnUserDTO toDto(ColumnUser entity, @MappingTarget ColumnUserDTO dto);

    @Named(value = "stringToArray")
    default List<String> stringToArray(String list){
        List<String> arrayList = new ArrayList<>();
        arrayList = Arrays.asList(list.split("}"));
        return arrayList;
    }

}
