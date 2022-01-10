package com.cooksys.social_media_project.services;

import com.cooksys.social_media_project.dtos.CredentialsRequestDto;
import com.cooksys.social_media_project.entities.User;
import com.cooksys.social_media_project.exceptions.NotAuthorizedException;

public interface AuthService {

    public User authenticate(CredentialsRequestDto credentialsRequestDto) throws NotAuthorizedException;

}
