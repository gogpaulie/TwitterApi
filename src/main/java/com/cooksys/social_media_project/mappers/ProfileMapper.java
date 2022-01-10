package com.cooksys.social_media_project.mappers;

import com.cooksys.social_media_project.dtos.ProfileDto;
import com.cooksys.social_media_project.entities.Profile;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    Profile requestDtoToEntity(ProfileDto profileDto);

    ProfileDto entityToResponseDto(Profile profile);


}

