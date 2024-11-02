package com.mindshare.app.platform.api.service.me.impl;

import com.mindshare.app.platform.api.dto.me.MeDetailResponseDto;
import com.mindshare.app.platform.api.dto.me.MeUpdateRequestDto;
import com.mindshare.app.platform.api.repository.CategoryRepository;
import com.mindshare.app.platform.api.repository.UserRepository;
import com.mindshare.app.platform.api.repository.UserTypeRepository;
import com.mindshare.app.platform.api.securityUser.SecurityUser;
import com.mindshare.app.platform.api.service.SecurityServiceImpl;
import com.mindshare.app.platform.api.service.me.MeService;
import com.mindshare.domain.system.entity.Category;
import com.mindshare.domain.user.entity.User;
import com.mindshare.domain.user.entity.UserType;
import io.client.core.dto.SuccessResponseDto;
import io.system.core.exception.ApiException;
import io.system.core.exception.enums.ApiExceptionEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(rollbackFor = Exception.class)
@Slf4j
@RequiredArgsConstructor
public class MeServiceImpl implements MeService {
  private final UserRepository userRepository;
  private final UserTypeRepository userTypeRepository;
  private final CategoryRepository categoryRepository;
  private final PasswordEncoder passwordEncoder;


  @Override
  public MeDetailResponseDto getMe() {

    // 토큰에서 유저 pk 가져오기
    BigInteger userId = this.getUserId();

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

  @Override
  public SuccessResponseDto updateMe(MeUpdateRequestDto body) {
    // 토큰에서 user pk 가져오기
    BigInteger userId = this.getUserId();

    // 정보 변경할 사용자 가져오기
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ApiException(ApiExceptionEnum.USER_NOT_FOUND));

    // 비밀번호 변경할 경우 encoding
    String password = null;
    if(body.getPassword() != null) {
      password = passwordEncoder.encode(body.getPassword());
    }

    // 사용자타입 가져오기
    UserType userType = null;
    if(body.getUserType() != null) {
      userType = userTypeRepository.findByName(body.getUserType().getName())
          .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND));
    }

    // 사용자-분야 가져오기
    List<Category> categories = null;
    if(body.getIsCategoriesDirty() && body.getCategories() != null) {
      categories = new ArrayList<>();
      for (String categoryLabel : body.getCategories()) {
        // 분야 조회
        Category category = categoryRepository.findByLabel(categoryLabel)
            .orElseThrow(() -> new ApiException(ApiExceptionEnum.NOT_FOUND));

        categories.add(category);
      }
    }

    // user에 변경된 정보 적용
    user.update(body.getEmailAddress(), body.getPhoneNumber(), password, userType,
        categories, body.getIsCategoriesDirty(), body.getName(), body.getBirthDate(), body.getIsBirthDateDirty(),
        body.getAlarmAgreedDatetime(), body.getIsAlarmAgreedDatetimeDirty());

    // user update
    userRepository.save(user);

    return SuccessResponseDto.builder()
        .success(true)
        .build();
  }

  private BigInteger getUserId() {
    // 토큰에서 유저 pk 가져오기
    SecurityUser securityUser = SecurityServiceImpl.getSecurityUserByToken();
    return  securityUser.getId();
  }
}
