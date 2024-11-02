package com.mindshare.app.platform.api.service.user;


import com.mindshare.app.platform.api.dto.user.*;

public interface AuthService {
  JoinResponseDto join(JoinRequestDto body);

  LoginResponseDto login(LoginRequestDto body);

  DuplicateCheckResponseDto duplicateCheckEmail(DuplicateCheckRequestDto body);
  DuplicateCheckResponseDto duplicateCheckPhone(DuplicateCheckRequestDto body);

  ReissueTokenResponseDto reissueToken(String refreshToken);

}
