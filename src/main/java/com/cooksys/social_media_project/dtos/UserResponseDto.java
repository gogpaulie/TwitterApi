package com.cooksys.social_media_project.dtos;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@NoArgsConstructor
@Data
public class UserResponseDto {

    private Long id;

    private CredentialsResponseDto credentials;

    private ProfileDto profile;

    private Timestamp joined;
}
