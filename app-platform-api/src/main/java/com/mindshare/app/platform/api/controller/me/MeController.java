package com.mindshare.app.platform.api.controller.me;

import com.mindshare.app.platform.api.dto.me.MeDetailResponseDto;
import com.mindshare.app.platform.api.service.me.MeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users/me")
public class MeController {
  private final MeService service;

  @GetMapping("")
  @ResponseStatus(HttpStatus.OK)
  public MeDetailResponseDto getMe() {
    return service.getMe();
  }
}
