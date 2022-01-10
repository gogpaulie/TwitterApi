package com.cooksys.social_media_project.services;

import com.cooksys.social_media_project.dtos.CredentialsRequestDto;
import com.cooksys.social_media_project.dtos.TweetResponseDto;
import com.cooksys.social_media_project.dtos.UserRequestDto;
import com.cooksys.social_media_project.dtos.UserResponseDto;

import java.util.List;

public interface UserService {

  List<UserResponseDto> getAllUsers();

  UserResponseDto addUser(UserRequestDto userRequestDto);

  UserResponseDto getUser(String username);

  UserResponseDto replaceUser(String username, UserRequestDto userRequestDto);

  UserResponseDto deleteUser(String username, CredentialsRequestDto credentialsRequestDto);

  void followUser(String username, CredentialsRequestDto credentialsRequestDto);

  void unfollowUser(String username, CredentialsRequestDto credentialsRequestDto);

  List<TweetResponseDto> retrieveUserFeed(String username);

  List<TweetResponseDto> retrieveUserTweets(String username);

  List<TweetResponseDto> retrieveUserMentions(String username);

  List<UserResponseDto> retrieveUserFollowers(String username);

  List<UserResponseDto> retrieveFollowing(String username);

}
