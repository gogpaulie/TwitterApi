package com.cooksys.social_media_project.mappers;

import com.cooksys.social_media_project.dtos.CredentialsRequestDto;
import com.cooksys.social_media_project.entities.Credentials;
import org.mapstruct.Mapper;

@Mapper (componentModel = "spring")
public interface CredentialsMapper {

    Credentials requestToEntity(CredentialsRequestDto credentialsRequestDto);

    CredentialsRequestDto entityToResponseDto(Credentials credentials);


}
