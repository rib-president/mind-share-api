package com.mindshare.app.platform.api.service;

import com.mindshare.app.platform.api.repository.SecurityRepository;
import com.mindshare.app.platform.api.securityUser.SecurityUser;
import com.mindshare.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@RequiredArgsConstructor
@Service
public class SecurityServiceImpl implements UserDetailsService {
  private final SecurityRepository securityRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    BigInteger userId = new BigInteger(username);
    User user = securityRepository.findByUserId(userId)
        .orElseThrow(() -> new UsernameNotFoundException(username + " 사용자를 찾을 수 없습니다."));

    return SecurityUser.builder()
        .username(user.getUserId().toString())
        .password(null)
        .authorities(null)
        .id(userId)
        .build();
  }

  public static SecurityUser getSecurityUserByToken() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    SecurityUser securityUser = (SecurityUser) authentication.getPrincipal();

    return securityUser;
  }
}
