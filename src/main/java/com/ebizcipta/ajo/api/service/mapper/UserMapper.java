package com.ebizcipta.ajo.api.service.mapper;


import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.service.dto.AuthTokenCustomDTO;
import com.ebizcipta.ajo.api.service.dto.UserCreateDTO;
import com.ebizcipta.ajo.api.service.dto.UserListDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "beneficiary.namaBeneficiary", target = "beneficiaryName")
    @Mapping(source = "entity.creationDate", target = "createDate", qualifiedByName = "tanggalInstantToLong")
    UserCreateDTO toDto(User entity, @MappingTarget UserCreateDTO dto);

    @Mapping(source = "beneficiary.namaBeneficiary", target = "beneficiaryName")
    @Mapping(source = "entity.creationDate", target = "createDate", qualifiedByName = "tanggalInstantToLong")
    UserListDTO toDto(User entity, @MappingTarget UserListDTO dto);

    AuthTokenCustomDTO toDtoAuth(User entity, @MappingTarget AuthTokenCustomDTO dto);

    User toEntity(UserCreateDTO dto, @MappingTarget User entity);

    User toEntityObject(Object object, @MappingTarget User entity);

    @Named("tanggalInstantToLong")
    default Long tanggalInstantToLong(Instant tanggalInstant) {
        if (tanggalInstant == null){
            return 0L;
        }else {
            return tanggalInstant.toEpochMilli();
        }
    }

}
