package com.cooksys.social_media_project.controllers;

import com.cooksys.social_media_project.dtos.CredentialsRequestDto;
import com.cooksys.social_media_project.dtos.TweetResponseDto;
import com.cooksys.social_media_project.dtos.UserRequestDto;
import com.cooksys.social_media_project.dtos.UserResponseDto;
import com.cooksys.social_media_project.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

  private final UserService userService;

  @GetMapping()
  public List<UserResponseDto> getUsers() {
    return userService.getAllUsers();
  }

  @PostMapping()
  public UserResponseDto createUser(@RequestBody UserRequestDto userRequestDto) {
    return userService.addUser(userRequestDto);
  }

  @GetMapping(value = "/@{username}")
  public UserResponseDto getUser(@PathVariable String username) {
    return userService.getUser(username);
  }

  @PatchMapping(value = "/@{username}")
  public UserResponseDto updateUser(@PathVariable String username, @RequestBody UserRequestDto userRequestDto) {
    return userService.replaceUser(username, userRequestDto);
  }

  @DeleteMapping(value = "/@{username}")
  public UserResponseDto removeUser(@PathVariable String username, @RequestBody CredentialsRequestDto credentialsRequestDto) {
    return userService.deleteUser(username, credentialsRequestDto);
  }

  @PostMapping(value = "/@{username}/follow")
  public void followUser(@PathVariable String username, @RequestBody CredentialsRequestDto credentialsRequestDto) {
    userService.followUser(username, credentialsRequestDto);
  }

  @PostMapping(value = "/@{username}/unfollow")
  public void unfollowUser(@PathVariable String username, @RequestBody CredentialsRequestDto credentialsRequestDto) {
    userService.unfollowUser(username, credentialsRequestDto);
  }

  @GetMapping(value = "/@{username}/feed")
  public List<TweetResponseDto> retrieveUserFeed(@PathVariable String username) {
    return userService.retrieveUserFeed(username);
  }

  @GetMapping(value = "/@{username}/tweets")
  public List<TweetResponseDto> retrieveUserTweets(@PathVariable String username) {
    return userService.retrieveUserTweets(username);
  }

  @GetMapping(value = "/@{username}/mentions")
  public List<TweetResponseDto> retrieveUserMentions(@PathVariable String username) {
    return userService.retrieveUserMentions(username);
  }

  @GetMapping(value = "/@{username}/followers")
  public List<UserResponseDto> retrieveUserFollowers(@PathVariable String username) {
    return userService.retrieveUserFollowers(username);
  }

  @GetMapping(value = "/@{username}/following")
  public List<UserResponseDto> retrieveFollowing(@PathVariable String username) {
    return userService.retrieveFollowing(username);
  }

}
