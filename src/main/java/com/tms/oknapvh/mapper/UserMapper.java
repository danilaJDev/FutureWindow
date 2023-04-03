package com.tms.oknapvh.mapper;

import com.tms.oknapvh.dto.UserDto;
import com.tms.oknapvh.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {

    UserEntity dtoToEntity(UserDto userDto);

    UserDto entityToDto(UserEntity userEntity);

}
