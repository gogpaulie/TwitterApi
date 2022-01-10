package com.cooksys.social_media_project.services;

import com.cooksys.social_media_project.entities.Hashtag;
import com.cooksys.social_media_project.entities.User;
import com.cooksys.social_media_project.exceptions.BadRequestException;
import com.cooksys.social_media_project.repositories.HashTagRepository;
import com.cooksys.social_media_project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ValidateServiceImpl implements ValidateService {

  private final HashTagRepository hashTagRepository;
  private final UserRepository userRepository;

  @Override
  public Boolean checkIfTagExists(String label) {
    Optional<Hashtag> optionalHashtag = hashTagRepository.findOneByLabelIgnoreCase(label);
    if (optionalHashtag.isPresent()) {
      Hashtag hashtag1 = optionalHashtag.get();
      if (hashtag1.getLabel().equals(label)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Boolean checkIfUserExists(String username) {
    Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (user.getCredentials().getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Boolean checkIfUserAvailable(String username) {
    Optional<User> optionalUser = userRepository.findByCredentialsUsernameAndDeletedFalse(username);
    if (optionalUser.isPresent()) {
      User user = optionalUser.get();
      if (user.getCredentials().getUsername().equals(username)) {
        throw new BadRequestException("A user with username : " + username + " already exists");
      }
    }
    return true;
  }

}
