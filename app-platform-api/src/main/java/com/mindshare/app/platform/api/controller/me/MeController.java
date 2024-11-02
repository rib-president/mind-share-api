package com.mindshare.app.platform.api.controller.me;

import com.mindshare.app.platform.api.dto.me.MeDetailResponseDto;
import com.mindshare.app.platform.api.dto.me.MeUpdateRequestDto;
import com.mindshare.app.platform.api.service.me.MeService;
import io.client.core.dto.SuccessResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

  @PatchMapping("")
  @ResponseStatus(HttpStatus.OK)
  public SuccessResponseDto updateMe(@Valid @RequestBody MeUpdateRequestDto body) {
    return service.updateMe(body);
  }
}
