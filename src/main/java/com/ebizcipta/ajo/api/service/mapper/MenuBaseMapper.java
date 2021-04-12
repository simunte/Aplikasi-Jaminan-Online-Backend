package com.ebizcipta.ajo.api.service.mapper;


import com.ebizcipta.ajo.api.domain.Menu;
import com.ebizcipta.ajo.api.service.dto.MenuBaseDTO;
import com.ebizcipta.ajo.api.service.dto.MenuListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "string")
public interface MenuBaseMapper extends EntityMapper<MenuListDTO, Menu> {

    MenuBaseMapper INSTANCE = Mappers.getMapper(MenuBaseMapper.class);
}
