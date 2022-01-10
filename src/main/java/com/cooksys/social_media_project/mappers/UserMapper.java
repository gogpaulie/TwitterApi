package com.cooksys.social_media_project.mappers;

import com.cooksys.social_media_project.dtos.UserRequestDto;
import com.cooksys.social_media_project.dtos.UserResponseDto;
import com.cooksys.social_media_project.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ProfileMapper.class, CredentialsMapper.class })
public interface UserMapper {

    User requestDtoToEntity(UserRequestDto userRequestDto);

    UserResponseDto entityToResponseDto(User user);

    List<UserResponseDto> entitiesToResponseDtos(List<User> users);

}
