package com.cooksys.social_media_project.controllers;

import com.cooksys.social_media_project.dtos.*;
import com.cooksys.social_media_project.services.TweetService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/tweets")
public class TweetController {

  private final TweetService tweetService;

  @GetMapping()
  public List<TweetResponseDto> getTweets() {
    return tweetService.getAllTweets();
  }

  @PostMapping
  public TweetResponseDto addTweet(@RequestBody TweetRequestDto tweetRequestDto) {
    return tweetService.createTweet(tweetRequestDto.getCredentials(), tweetRequestDto.getContent());
  }

  @GetMapping(value = "/{id}")
  public TweetResponseDto getTweetById(@PathVariable("id") Long id) {
    return tweetService.getTweetById(id);
  }

  @DeleteMapping(value = "/{id}")
  public TweetResponseDto removeTweet(@PathVariable("id") Long id, @RequestBody CredentialsRequestDto credentialsRequestDto) {
    return tweetService.deleteTweet(id, credentialsRequestDto);
  }

  @PostMapping(value = "/{id}/like")
  public void addLike(@RequestBody() CredentialsRequestDto credentialsRequestDto, @PathVariable("id") Long id) {
    tweetService.addLike(id, credentialsRequestDto);
  }

  @PostMapping(value = "/{id}/reply")
  public TweetResponseDto addReply(@PathVariable Long id, @RequestBody TweetRequestDto tweetRequestDto) {
    return tweetService.addReply(id, tweetRequestDto.getCredentials(), tweetRequestDto.getContent());
  }

  @PostMapping(value = "/{id}/repost")
  public TweetResponseDto createRepost(@PathVariable("id") Long id, @RequestBody CredentialsRequestDto credentialsRequestDto) {
    return tweetService.createRepost(id, credentialsRequestDto);
  }

  @GetMapping(value = "/{id}/tags")
  public List<HashTagDto> getTagsByTweetId(@PathVariable("id") Long id) {
    return tweetService.getTagsByTweetId(id);
  }

  @GetMapping(value = "/{id}/likes")
  public List<UserResponseDto> getUsersWhoLikedTweet(@PathVariable("id") Long id) {
    return tweetService.getUsersWhoLikedTweet(id);
  }

  @GetMapping(value = "/{id}/context")
  public ContextDto getContext(@PathVariable("id") Long id) {
    return tweetService.getContext(id);
  }

  @GetMapping(value = "/{id}/replies")
  public List<TweetResponseDto> getReplies(@PathVariable("id") Long id) {
    return tweetService.getReplies(id);
  }

  @GetMapping(value = "/{id}/reposts")
  public List<TweetResponseDto> getReposts(@PathVariable("id") Long id) {
    return tweetService.getReposts(id);
  }

  @GetMapping(value = "/{id}/mentions")
  public List<UserResponseDto> getMentions(@PathVariable("id") Long id) {
    return tweetService.getMentions(id);
  }

}
