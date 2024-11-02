package com.mindshare.app.platform.api.service.auth;

import com.mindshare.app.platform.api.dto.auth.*;

public interface AuthService {
  JoinResponseDto join(JoinRequestDto body);

  LoginResponseDto login(LoginRequestDto body);

  DuplicateCheckResponseDto duplicateCheckEmail(DuplicateCheckRequestDto body);
  DuplicateCheckResponseDto duplicateCheckPhone(DuplicateCheckRequestDto body);

}
