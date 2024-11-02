package com.mindshare.app.platform.api.service.auth;

import com.mindshare.app.platform.api.dto.auth.JoinRequestDto;
import com.mindshare.app.platform.api.dto.auth.JoinResponseDto;
import com.mindshare.app.platform.api.dto.auth.LoginRequestDto;
import com.mindshare.app.platform.api.dto.auth.LoginResponseDto;

public interface AuthService {
  JoinResponseDto join(JoinRequestDto body);

  LoginResponseDto login(LoginRequestDto body);

}
