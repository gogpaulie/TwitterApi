package com.cooksys.social_media_project.mappers;

import com.cooksys.social_media_project.dtos.TweetRequestDto;
import com.cooksys.social_media_project.dtos.TweetResponseDto;
import com.cooksys.social_media_project.entities.Tweet;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TweetMapper {

    Tweet requestDtoToEntity(TweetRequestDto tweetRequestDto);

    TweetResponseDto entityToResponseDto(Tweet tweet);

    List<TweetResponseDto> entitiesToResponseDtos(List<Tweet> tweets);

}
