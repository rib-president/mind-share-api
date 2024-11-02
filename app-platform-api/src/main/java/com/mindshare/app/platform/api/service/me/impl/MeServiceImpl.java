package com.mindshare.app.platform.api.service.me.impl;

import com.mindshare.app.platform.api.dto.me.MeDetailResponseDto;
import com.mindshare.app.platform.api.repository.UserRepository;
import com.mindshare.app.platform.api.securityUser.SecurityUser;
import com.mindshare.app.platform.api.service.SecurityServiceImpl;
import com.mindshare.app.platform.api.service.me.MeService;
import com.mindshare.domain.user.entity.User;
import io.system.core.exception.ApiException;
import io.system.core.exception.enums.ApiExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class MeServiceImpl implements MeService {
  private final UserRepository userRepository;

  @Override
  public MeDetailResponseDto getMe() {

    // 토큰에서 유저 pk 가져오기
    SecurityUser securityUser = SecurityServiceImpl.getSecurityUserByToken();
    BigInteger userId = securityUser.getId();

    // 유저 정보 가져오기
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.USER_NOT_FOUND));


    return MeDetailResponseDto.builder()
        .emailAddress(user.getEmailAddress())
        .phoneNumber(user.getPhoneNumber())
        .userType(user.getUserType().getLabel())
        .categories(user.getUserCategories().stream()
            .map(userCategory -> userCategory.getCategory().getLabel())
            .toList())
        .name(user.getName())
        .birthDate(user.getBirthDate())
        .alarmAgreedDatetime(user.getAlarmAgreedDatetime())
        .build();
  }
}
