package com.mindshare.app.platform.api.service.auth.impl;

import com.mindshare.app.platform.api.dto.auth.JoinRequestDto;
import com.mindshare.app.platform.api.dto.auth.JoinResponseDto;
import com.mindshare.app.platform.api.repository.CategoryRepository;
import com.mindshare.app.platform.api.repository.UserRepository;
import com.mindshare.app.platform.api.repository.UserTypeRepository;
import com.mindshare.app.platform.api.service.auth.AuthService;
import com.mindshare.domain.system.entity.Category;
import com.mindshare.domain.user.entity.User;
import com.mindshare.domain.user.entity.UserType;
import io.client.core.provider.JwtProvider;
import io.system.core.exception.ApiException;
import io.system.core.exception.enums.ApiExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final UserRepository userRepository;
  private final UserTypeRepository userTypeRepository;
  private final CategoryRepository categoryRepository;

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
    for(String categoryLabel : body.getCategories()) {
      // 분야 조회
      Category category = categoryRepository.findByLabel(categoryLabel)
          .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND));

      user.addUserCategory(category);
    }

    // insert
    userRepository.save(user);

    // 토큰 발급
    String accessToken = jwtProvider.generateAccessToken(user.getUserId().toString(), new ArrayList<>());
    String refreshToken = jwtProvider.generateRefreshToken(user.getUserId().toString(), new ArrayList<>());

    return JoinResponseDto.builder()
        .accessToken(accessToken)
        .refreshToken(refreshToken)
        .build();
  }
}
