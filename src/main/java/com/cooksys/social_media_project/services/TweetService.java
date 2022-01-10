package com.cooksys.social_media_project.services;

import com.cooksys.social_media_project.dtos.*;

import java.util.List;

public interface TweetService {

  List<TweetResponseDto> getAllTweets();

  TweetResponseDto createTweet(CredentialsRequestDto credentialsRequestDto, String content);

  TweetResponseDto getTweetById(Long id);

  TweetResponseDto deleteTweet(Long id, CredentialsRequestDto credentialsRequestDto);

  void addLike(Long id, CredentialsRequestDto credentialsRequestDto);

  TweetResponseDto addReply(Long id, CredentialsRequestDto credentialsRequestDto, String content);

  TweetResponseDto createRepost(Long id, CredentialsRequestDto credentialsRequestDto);

  List<HashTagDto> getTagsByTweetId(Long id);

  List<UserResponseDto> getUsersWhoLikedTweet(Long id);

  ContextDto getContext(Long id);

  List<TweetResponseDto> getReplies(Long id);

  List<TweetResponseDto> getReposts(Long id);

  List<UserResponseDto> getMentions(Long id);

}
