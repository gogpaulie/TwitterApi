package com.cooksys.social_media_project.services;

import com.cooksys.social_media_project.dtos.HashTagDto;
import com.cooksys.social_media_project.dtos.TweetResponseDto;
import com.cooksys.social_media_project.entities.Hashtag;

import java.util.List;

public interface HashtagService {

  List<HashTagDto> getAllTags();

  List<TweetResponseDto> getTagsByLabel(String label);

}
