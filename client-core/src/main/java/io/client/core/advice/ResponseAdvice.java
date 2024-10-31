package io.client.core.advice;

import io.client.core.dto.ResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
  @Autowired
  private HttpServletRequest servletRequest;

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }

  @Override
  public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
    ResponseDto.ResponseDtoBuilder builder = ResponseDto.builder();

    builder.uri(servletRequest.getMethod() + " " + request.getURI().getPath());
    builder.id(servletRequest.getAttribute("id"));

    if(body instanceof ProblemDetail problemDetail) {
      builder.error(problemDetail);
    } else {
      builder.data(body);
    }

    return builder.build();
  }
}
