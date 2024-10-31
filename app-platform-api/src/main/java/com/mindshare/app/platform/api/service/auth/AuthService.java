package com.mindshare.app.platform.api.service.auth;

import com.mindshare.app.platform.api.dto.auth.JoinRequestDto;
import com.mindshare.app.platform.api.dto.auth.JoinResponseDto;

public interface AuthService {
  JoinResponseDto join(JoinRequestDto body);
}
