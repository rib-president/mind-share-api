package io.client.core.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.client.core.dto.ResponseDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

// 인증되지 않은 사용자가 인증이 필요한 요청 엔드포인트로 접근했을 시 핸들링
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultAuthenticationEntryPoint implements AuthenticationEntryPoint {
  private final ObjectMapper objectMapper;

  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
    log.error("Not Authenticated Request", authException);
    log.error("Request Uri : {}", request.getRequestURI());

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.UNAUTHORIZED, "UNAUTHENTICATED"
    );
    problemDetail.setTitle("Unauthorized");
    problemDetail.setDetail(authException.getMessage());
    problemDetail.setInstance(URI.create(request.getRequestURI()));
    this.sendErrorResponse(request, response, problemDetail);
  }

  private void sendErrorResponse(HttpServletRequest request, HttpServletResponse response,
                                 ProblemDetail problemDetail) throws IOException {
    ResponseDto.ResponseDtoBuilder builder = ResponseDto.builder();
    builder.id(request.getAttribute("id"))
        .uri(request.getMethod() + " " + request.getRequestURI())
        .error(problemDetail);

    String body = objectMapper.writeValueAsString(builder.build());

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setCharacterEncoding("UTF-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(body);
    response.getWriter().flush();
    response.getWriter().close();
  }
}
