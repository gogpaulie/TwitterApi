package com.cooksys.social_media_project.controllers;

import com.cooksys.social_media_project.services.ValidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/validate")
public class ValidateController {

  private final ValidateService validateService;

  @GetMapping(value = "/tag/exists/{label}")
  public Boolean tagExists(@PathVariable("label") String label) {
    return validateService.checkIfTagExists(label);
  }

  @GetMapping(value = "/username/exists/@{username}")
  public Boolean userExists(@PathVariable("username") String username) {
    return validateService.checkIfUserExists(username);
  }

  @GetMapping(value = "/username/available/@{username}")
  public Boolean usernameAvailable(@PathVariable("username") String username) {
    return validateService.checkIfUserAvailable(username);
  }

}
