package com.mindshare.app.platform.api.service.auth.impl;

import com.mindshare.app.platform.api.dto.auth.JoinRequestDto;
import com.mindshare.app.platform.api.dto.auth.JoinResponseDto;
import com.mindshare.app.platform.api.dto.auth.LoginRequestDto;
import com.mindshare.app.platform.api.dto.auth.LoginResponseDto;
import com.mindshare.app.platform.api.repository.CategoryRepository;
import com.mindshare.app.platform.api.repository.UserRepository;
import com.mindshare.app.platform.api.repository.UserTypeRepository;
import com.mindshare.app.platform.api.service.auth.AuthService;
import com.mindshare.domain.system.entity.Category;
import com.mindshare.domain.user.entity.User;
import com.mindshare.domain.user.entity.UserType;
import io.client.core.entity.RefreshToken;
import io.client.core.provider.JwtProvider;
import io.client.core.repository.RefreshTokenRepository;
import io.system.core.exception.ApiException;
import io.system.core.exception.enums.ApiExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.misc.Pair;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final UserTypeRepository userTypeRepository;
  private final CategoryRepository categoryRepository;
  private final RefreshTokenRepository refreshTokenRepository;

  private final PasswordEncoder passwordEncoder;
  private final JwtProvider jwtProvider;

  @Override
  public JoinResponseDto join(JoinRequestDto body) {
    // 비밀번호 암호화
    String encodedPassword = passwordEncoder.encode(body.getPassword());

    // 사용자 타입 조회(STUDENT, EXPERT)
    UserType userType = userTypeRepository.findByName(body.getUserType().name())
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND));

    // 사용자 생성
    User user = User.builder()
        .emailAddress(body.getEmailAddress())
        .phoneNumber(body.getPhoneNumber())
        .password(encodedPassword)
        .userType(userType)
        .name(body.getName())
        .birthDate(body.getBirthDate())
        .alarmAgreedDatetime(body.getAlarmAgreedDatetime())
        .userCategories(new ArrayList<>())
        .build();

    // 사용자-분야 생성
    if (body.getCategories() != null) {
      for (String categoryLabel : body.getCategories()) {
        // 분야 조회
        Category category = categoryRepository.findByLabel(categoryLabel)
            .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND));

        user.addUserCategory(category);
      }
    }

    // insert
    userRepository.save(user);

    // 토큰 발급
    Pair<String, String> tokens = this.generateTokens(user.getUserId().toString(), new ArrayList<>());

    return JoinResponseDto.builder()
        .accessToken(tokens.a)
        .refreshToken(tokens.b)
        .build();
  }

  @Override
  public LoginResponseDto login(LoginRequestDto body) {

    // 로그인 타입에 따른 사용자 정보 가져오기
    User user = switch (body.getLoginType()) {
      case EMAIL -> userRepository.findByEmailAddress(body.getUsername()).orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND));
      case PHONE -> userRepository.findByPhoneNumber(body.getUsername()).orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND));
    };

    // 비밀번호 불일치
    if(!passwordEncoder.matches(body.getPassword(), user.getPassword())) {
      throw new ApiException(ApiExceptionEnum.PASSWORD_NOT_MATCH);
    }

    // 토큰 생성
    Pair<String, String> tokens = this.generateTokens(user.getUserId().toString(), new ArrayList<>());

    return LoginResponseDto.builder()
        .accessToken(tokens.a)
        .refreshToken(tokens.b)
        .build();
  }

  private Pair<String, String> generateTokens(String id, List<String> authorities) {
    String accessToken = jwtProvider.generateAccessToken(id, authorities);
    String refreshToken = jwtProvider.generateRefreshToken(id, new ArrayList<>());

    this.saveRefreshToken(id, refreshToken);

    return new Pair<>(accessToken, refreshToken);
  }

  // redis에 refresh token 저장
  private void saveRefreshToken(String id, String token) {
    RefreshToken rt = RefreshToken.builder()
        .userId(id)
        .token(token)
        .build();
    refreshTokenRepository.save(rt);
  }
}
