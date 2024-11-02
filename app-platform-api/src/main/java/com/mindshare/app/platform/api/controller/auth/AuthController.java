package com.mindshare.app.platform.api.controller.auth;

import com.mindshare.app.platform.api.dto.auth.*;
import com.mindshare.app.platform.api.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/auth")
public class AuthController {
  private final AuthService service;

  @PostMapping("/join")
  @ResponseStatus(HttpStatus.CREATED)
  public JoinResponseDto join(@Valid @RequestBody JoinRequestDto body) {
    return service.join(body);
  }

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public LoginResponseDto login(@Valid @RequestBody LoginRequestDto body) {
    return service.login(body);
  }

  @PostMapping("/duplicate-check/email")
  @ResponseStatus(HttpStatus.OK)
  public DuplicateCheckResponseDto duplicateCheckEmail(@Valid @RequestBody DuplicateCheckRequestDto body) {
    return service.duplicateCheckEmail(body);
  }

  @PostMapping("/duplicate-check/phone")
  @ResponseStatus(HttpStatus.OK)
  public DuplicateCheckResponseDto duplicateCheckPhone(@Valid @RequestBody DuplicateCheckRequestDto body) {
    return service.duplicateCheckPhone(body);
  }
}
