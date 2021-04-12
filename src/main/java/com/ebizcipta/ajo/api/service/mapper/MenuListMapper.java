package com.ebizcipta.ajo.api.service.mapper;

import com.ebizcipta.ajo.api.domain.Menu;
import com.ebizcipta.ajo.api.service.dto.MenuListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "string")
public interface MenuListMapper extends EntityMapper<MenuListDTO, Menu>{

    MenuListMapper INSTANCE = Mappers.getMapper(MenuListMapper.class);

}
