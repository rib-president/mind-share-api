package com.mindshare.app.platform.api.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;
import java.sql.Date;
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

      SimpleModule sqlDateModule = new SimpleModule();
      sqlDateModule.addDeserializer(Date.class, new SqlDateDeserializer());
      objectMapper.registerModule(sqlDateModule);

      MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(objectMapper);
      converters.add(0, converter);
    }

    // java.sql.Date를 위한 커스텀 deserializer
    private static class SqlDateDeserializer extends StdDeserializer<Date> {
      public SqlDateDeserializer() {
        this(null);
      }

      public SqlDateDeserializer(Class<?> vc) {
        super(vc);
      }

      @Override
      public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
        String date = jp.getText();
        try {
          return Date.valueOf(date);
        } catch (Exception e) {
          throw new IOException("Failed to parse date: " + date, e);
        }
      }
    }

}
