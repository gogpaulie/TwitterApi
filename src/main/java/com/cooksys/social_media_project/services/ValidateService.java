package com.cooksys.social_media_project.services;

public interface ValidateService {

  Boolean checkIfTagExists(String label);

  Boolean checkIfUserExists(String username);

  Boolean checkIfUserAvailable(String username);
}
