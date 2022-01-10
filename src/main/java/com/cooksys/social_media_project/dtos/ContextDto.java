package com.cooksys.social_media_project.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class ContextDto {

    private TweetResponseDto target;

    private List<TweetResponseDto> before;

    private List<TweetResponseDto> after;
}
