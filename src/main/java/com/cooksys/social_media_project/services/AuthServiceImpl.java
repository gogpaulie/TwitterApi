package com.cooksys.social_media_project.services;

import com.cooksys.social_media_project.dtos.CredentialsRequestDto;
import com.cooksys.social_media_project.entities.User;
import com.cooksys.social_media_project.exceptions.NotAuthorizedException;
import com.cooksys.social_media_project.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public User authenticate(CredentialsRequestDto credentialsRequestDto) throws NotAuthorizedException {
        final var credentials = Optional.ofNullable(credentialsRequestDto);
        final var username = credentials.map(CredentialsRequestDto::getUsername).orElse("");
        final var password = credentials.map(CredentialsRequestDto::getPassword).orElse("");
        return userRepository.findByCredentialsUsernameAndDeletedFalse(username)
                .filter(u -> u.getCredentials().getPassword().equals(password))
                .orElseThrow(() -> new NotAuthorizedException("Credentials invalid"));
    }
}
