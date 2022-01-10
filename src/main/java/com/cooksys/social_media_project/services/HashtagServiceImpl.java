package com.cooksys.social_media_project.services;

import com.cooksys.social_media_project.dtos.HashTagDto;
import com.cooksys.social_media_project.dtos.TweetResponseDto;
import com.cooksys.social_media_project.entities.Hashtag;
import com.cooksys.social_media_project.entities.Tweet;
import com.cooksys.social_media_project.exceptions.NotFoundException;
import com.cooksys.social_media_project.mappers.HashtagMapper;
import com.cooksys.social_media_project.mappers.TweetMapper;
import com.cooksys.social_media_project.repositories.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashtagServiceImpl implements HashtagService {

  private final HashTagRepository hashTagRepository;
  private final HashtagMapper hashtagMapper;
  private final TweetMapper tweetMapper;

  @Override
  public List<HashTagDto> getAllTags() {
    return hashtagMapper.entitiesToResponseDtos(hashTagRepository.findAll());
  }

  @Override
  public List<TweetResponseDto> getTagsByLabel(String label) {
    Optional<Hashtag> hashtag = Optional.ofNullable(hashTagRepository.findByLabelIgnoreCase(label));
    if (!hashtag.isPresent()) {
      throw new NotFoundException("Hashtag not found");
    }
    List<Tweet> tweets = hashtag.get().getTweets();

    Collections.reverse(tweets);

    return tweetMapper.entitiesToResponseDtos(tweets);
  }

}
