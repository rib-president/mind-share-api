package com.mindshare.app.platform.api.service.user;

import com.mindshare.app.platform.api.dto.user.MeDetailResponseDto;
import com.mindshare.app.platform.api.dto.user.MeUpdateRequestDto;
import io.client.core.dto.SuccessResponseDto;

public interface MeService {
  MeDetailResponseDto getMe();

  SuccessResponseDto updateMe(MeUpdateRequestDto body);
}
