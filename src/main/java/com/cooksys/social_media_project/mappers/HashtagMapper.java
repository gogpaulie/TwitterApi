package com.cooksys.social_media_project.mappers;

import com.cooksys.social_media_project.dtos.HashTagDto;
import com.cooksys.social_media_project.entities.Hashtag;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface HashtagMapper {

    Hashtag requestDtoToEntity(HashTagDto hashTagDto);

    List<HashTagDto> entitiesToResponseDtos(List<Hashtag> hashtags);

}
