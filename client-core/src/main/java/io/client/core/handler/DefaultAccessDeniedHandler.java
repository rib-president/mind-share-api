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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;


// 인증은 완료하였으나 요청에 대해 권한을 가지고 있지 않은 사용자가 접근하려고 할 때
@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultAccessDeniedHandler implements AccessDeniedHandler {
  private final ObjectMapper objectMapper;

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
    log.error("Not Authorized Request", accessDeniedException);
    log.error("Request Uri : {}", request.getRequestURI());

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
        HttpStatus.FORBIDDEN, "UNAUTHORIZED");
    problemDetail.setTitle("Forbidden");
    problemDetail.setDetail(accessDeniedException.getMessage());
    problemDetail.setInstance(URI.create(request.getRequestURI()));
    this.sendErrorResonse(request, response, problemDetail);
  }

  private void sendErrorResonse(HttpServletRequest request, HttpServletResponse response, ProblemDetail problemDetail) throws IOException {

    ResponseDto.ResponseDtoBuilder builder = ResponseDto.builder();
    builder.id(request.getAttribute("id"))
        .uri(request.getMethod() + " " + request.getRequestURI())
        .error(problemDetail);

    String body = objectMapper.writeValueAsString(builder.build());

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setCharacterEncoding("UTF-8");
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(body);
    response.getWriter().flush();
    response.getWriter().close();
  }
}
