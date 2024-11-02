package com.mindshare.app.platform.api.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Configuration
@EnableWebMvc
public class WebMvcConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
        .allowedOriginPatterns("*")
        .allowedMethods("GET", "POST", "PATCH", "DELETE")
        .allowedHeaders("Authorization", "Content-Type", "X-Refresh-Token")
        .allowCredentials(true)
        .maxAge(3600);
  }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
      ObjectMapper objectMapper = new ObjectMapper();

      objectMapper.setTimeZone(TimeZone.getTimeZone("UTC"));

      objectMapper.registerModule(new JavaTimeModule());
      objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
      objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
      objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);

      MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
      converters.add(0, converter);
    }
}
