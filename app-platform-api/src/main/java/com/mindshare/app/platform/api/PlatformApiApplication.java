package com.mindshare.app.platform.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.mindshare.domain.**",
  "io.client.**"})
@EnableJpaRepositories(basePackages = {"com.mindshare.app.platform.api.repository"})
@EntityScan({"com.mindshare.domain.**"})
public class PlatformApiApplication {
  public static void main(String[] args) {

    SpringApplication.run(PlatformApiApplication.class, args);
  }
}