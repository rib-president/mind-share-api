package com.mindshare.app.platform.api.service.me;

import com.mindshare.app.platform.api.dto.me.MeDetailResponseDto;
import com.mindshare.app.platform.api.dto.me.MeUpdateRequestDto;
import io.client.core.dto.SuccessResponseDto;

public interface MeService {
  MeDetailResponseDto getMe();

  SuccessResponseDto updateMe(MeUpdateRequestDto body);
}
