package com.mindshare.app.platform.api.controller.auth;

import com.mindshare.app.platform.api.dto.auth.JoinRequestDto;
import com.mindshare.app.platform.api.dto.auth.JoinResponseDto;
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
    System.out.println("HI");
    return service.join(body);
  }
}
