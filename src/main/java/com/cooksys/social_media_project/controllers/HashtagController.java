package com.cooksys.social_media_project.controllers;

import com.cooksys.social_media_project.dtos.HashTagDto;
import com.cooksys.social_media_project.dtos.TweetResponseDto;
import com.cooksys.social_media_project.entities.Hashtag;
import com.cooksys.social_media_project.services.HashtagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tags")
public class HashtagController {

  private final HashtagService hashtagService;

  @GetMapping()
  public List<HashTagDto> getTags() {
    return hashtagService.getAllTags();
  }

  @GetMapping("/{label}")
  public List<TweetResponseDto> getTagsBylabel(@PathVariable("label") String label) {
    return hashtagService.getTagsByLabel(label);
  }

}
