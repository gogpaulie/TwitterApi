package com.cooksys.social_media_project.services;

import com.cooksys.social_media_project.dtos.CredentialsRequestDto;
import com.cooksys.social_media_project.dtos.TweetResponseDto;
import com.cooksys.social_media_project.dtos.UserRequestDto;
import com.cooksys.social_media_project.dtos.UserResponseDto;
import com.cooksys.social_media_project.entities.Tweet;
import com.cooksys.social_media_project.entities.User;
import com.cooksys.social_media_project.exceptions.BadRequestException;
import com.cooksys.social_media_project.exceptions.NotFoundException;
import com.cooksys.social_media_project.mappers.TweetMapper;
import com.cooksys.social_media_project.mappers.UserMapper;
import com.cooksys.social_media_project.repositories.TweetRepository;
import com.cooksys.social_media_project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final TweetRepository tweetRepository;
  private final TweetMapper tweetMapper;
  private final AuthService authService;

  private User validateUsername(String username, String message) {
    User user = userRepository.findByCredentialsUsername(username)
      .orElseThrow(() -> new NotFoundException(message));

    if (user.isDeleted()) {
      throw new NotFoundException("User Is Inactive");
    }

    return user;
  }

  private void checkMatchBetweenRequestVariables(String s1, String s2, Boolean mustMatch) {
    if (!s1.contains(s2) && mustMatch) {
      throw new BadRequestException("path variable username: " + s1 + " does not match @RequestBody Credentials username: " + s2);
    } else if (s1.contains(s2) && !mustMatch) {
      throw new BadRequestException("path variable username: " + s1 + " cannot follow themself.");
    }
  }

  @Override
  public List<UserResponseDto> getAllUsers() {
    return userMapper.entitiesToResponseDtos(userRepository.findAllByDeletedFalse());
  }

  @Override
  public UserResponseDto addUser(UserRequestDto userRequestDto) {
    final User user = userMapper.requestDtoToEntity(userRequestDto);
    if (user.getCredentials() == null || user.getProfile() == null) {
      throw new BadRequestException("Missing required fields in order to add user.");
    }
    final Optional<User> optionalUser = userRepository.findByCredentialsUsername(user.getCredentials().getUsername());
    if (optionalUser.isPresent()) {
      if (optionalUser.get().isDeleted()) {
        optionalUser.get().setDeleted(false);
        return userMapper.entityToResponseDto(userRepository.save(optionalUser.get()));
      } else {
        throw new BadRequestException("User already exists with those credentials. Please login.");
      }
    }
    return userMapper.entityToResponseDto(userRepository.save(user));
  }

  @Override
  public UserResponseDto getUser(String username) {
    User user = validateUsername(username, "Invalid User");
    return userMapper.entityToResponseDto(user);
  }

  @Override
  public UserResponseDto replaceUser(String username, UserRequestDto userRequestDto) {
    checkMatchBetweenRequestVariables(username, userRequestDto.getCredentials().getUsername(), true);
    validateUsername(username, "Invalid User");
    authService.authenticate(userRequestDto.getCredentials());
    final User replacementUser = userMapper.requestDtoToEntity(userRequestDto);
    final User user = userRepository.findByCredentialsUsername(username).get();
    user.setCredentials(replacementUser.getCredentials());
    user.setProfile(replacementUser.getProfile());
    return userMapper.entityToResponseDto(userRepository.save(user));
  }

  @Override
  public UserResponseDto deleteUser(String username, CredentialsRequestDto credentialsRequestDto) {
    checkMatchBetweenRequestVariables(username, credentialsRequestDto.getUsername(), true);
    validateUsername(username, "Invalid user");
    authService.authenticate(credentialsRequestDto);
    final Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
    if (optionalUser.isPresent()) {
      optionalUser.get().setDeleted(true);
    }
    return userMapper.entityToResponseDto(userRepository.save(optionalUser.get()));
  }

  @Override
  public void followUser(String username, CredentialsRequestDto credentialsRequestDto) {
    checkMatchBetweenRequestVariables(username, credentialsRequestDto.getUsername(), false);
    validateUsername(username, "Invalid user");
    authService.authenticate(credentialsRequestDto);
    final Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
    final Optional<User> optionalFollower = userRepository.findByCredentialsUsername(credentialsRequestDto.getUsername());
    if (optionalFollower.get().getFollowing().contains(optionalUser.get())) {
      throw new BadRequestException(username + " is already being followed by: " + credentialsRequestDto.getUsername());
    }
    optionalUser.get().getFollowers().add(optionalFollower.get());
    optionalFollower.get().getFollowing().add(optionalUser.get());
    userRepository.save(optionalUser.get());
    userRepository.save(optionalFollower.get());
  }

  @Override
  public void unfollowUser(String username, CredentialsRequestDto credentialsRequestDto) {
    checkMatchBetweenRequestVariables(username, credentialsRequestDto.getUsername(), false);
    validateUsername(username, "Invalid user");
    authService.authenticate(credentialsRequestDto);
    final Optional<User> optionalUser = userRepository.findByCredentialsUsername(username);
    final Optional<User> optionalFollower = userRepository.findByCredentialsUsername(credentialsRequestDto.getUsername());
    if (!optionalFollower.get().getFollowing().contains(optionalUser.get())) {
      throw new BadRequestException(username + " is already unfollowed by: " + credentialsRequestDto.getUsername());
    }
    optionalUser.get().getFollowers().remove(optionalFollower.get());
    optionalFollower.get().getFollowing().remove(optionalUser.get());
    userRepository.save(optionalUser.get());
    userRepository.save(optionalFollower.get());
  }

  @Override
  public List<TweetResponseDto> retrieveUserFeed(String username) {
    validateUsername(username, "Invalid user");
    final User user = userRepository.findByCredentialsUsername(username).get();
    final List<TweetResponseDto> userTweets = retrieveUserTweets(username);
    final List<UserResponseDto> followedUsers = retrieveFollowing(username);
    final List<TweetResponseDto> followedUsersTweets = new ArrayList<>();
    for (int i = 0; i < followedUsers.size(); i++) {
       followedUsersTweets.addAll(retrieveUserTweets(followedUsers.get(i).getCredentials().getUsername()));
    }
    final ArrayList<TweetResponseDto> feed = new ArrayList<>();
    feed.addAll(userTweets);
    feed.addAll(followedUsersTweets);
    return feed;
  }

  @Override
  public List<TweetResponseDto> retrieveUserTweets(String username) {
    validateUsername(username, "Invalid user");
    final User user = userRepository.findByCredentialsUsername(username).get();
    final ArrayList<TweetResponseDto> tweets = new ArrayList<>();
    for (int i = user.getTweets().size()-1; i >= 0; i--) {
      if (!user.getTweets().get(i).isDeleted()) {
        tweets.add(tweetMapper.entityToResponseDto(user.getTweets().get(i)));
      }
    }
    return tweets;
  }

  @Override
  public List<TweetResponseDto> retrieveUserMentions(String username) {
    validateUsername(username, "Invalid user");
    final User user = userRepository.findByCredentialsUsername(username).get();
    final ArrayList<TweetResponseDto> tweets = new ArrayList<>();
    for (int i = user.getMentions().size()-1; i >= 0; i--) {
      if (!user.getMentions().get(i).isDeleted()) {
        tweets.add(tweetMapper.entityToResponseDto(user.getMentions().get(i)));
      }
    }
    return tweets;
  }

  @Override
  public List<UserResponseDto> retrieveUserFollowers(String username) {
    validateUsername(username, "Invalid user");
    final User user = userRepository.findByCredentialsUsername(username).get();
    final ArrayList<UserResponseDto> followers = new ArrayList<>();
    for (User follower : user.getFollowers()) {
      if (!follower.isDeleted()) {
        followers.add(userMapper.entityToResponseDto(follower));
      }
    }
    return followers;
  }

  @Override
  public List<UserResponseDto> retrieveFollowing(String username) {
    validateUsername(username, "Invalid user");
    final User user = userRepository.findByCredentialsUsername(username).get();
    final ArrayList<UserResponseDto> followedUsers = new ArrayList<>();
    for (User follower : user.getFollowing()) {
      if (!user.isDeleted()) {
        followedUsers.add(userMapper.entityToResponseDto(follower));
      }
    }
    return followedUsers;
  }

}
