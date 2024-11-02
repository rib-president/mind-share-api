package io.client.core.filter;

import io.client.core.enums.JwtCode;
import io.client.core.exception.enums.ClientCoreExceptionEnum;
import io.client.core.provider.JwtProvider;
import io.system.core.exception.ApiException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class InitializeFilter extends OncePerRequestFilter {
  private final JwtProvider jwtProvider;


  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    String id = UUID.randomUUID().toString();
    request.setAttribute("id", id);

    String bearerToken = request.getHeader("Authorization");
    String accessToken = null;
    if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      accessToken = bearerToken.substring(7);
    }

    // access token 검증
    JwtCode validateCode = jwtProvider.validateToken(accessToken);

    // 사용가능한 access token
    if(StringUtils.hasText(accessToken) && JwtCode.ACCESS.equals(validateCode)) {
      Authentication authentication = jwtProvider.getAuthentication(accessToken);
      SecurityContextHolder.getContext().setAuthentication(authentication);

    } else if(StringUtils.hasText(accessToken) && JwtCode.EXPIRED.equals(validateCode)) {
      // 만료된 access token
      log.info("Access token expired");

      String refreshToken = request.getHeader("X-Refresh-Token");
      JwtCode refreshValidateCode = jwtProvider.validateToken(refreshToken);

      // refresh token이 header에 있고 만료되지 않음
      if(StringUtils.hasText(refreshToken) && JwtCode.ACCESS.equals(refreshValidateCode)) {
        // access token 재발급 요청 에러
        log.info("Token Reissue Required");
        throw new ApiException(ClientCoreExceptionEnum.EXPIRED_ACCESS_TOKEN);
      } else {
        // refresh token이 header에 없거나 만료되었을 때
        log.info("Required Re-Login");
        throw new ApiException(ClientCoreExceptionEnum.EXPIRED_REFRESH_TOKEN);
      }
    }

    filterChain.doFilter(request, response);
  }
}
