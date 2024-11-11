package io.client.core.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "io.client.core.repository")
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  @Value("${spring.data.redis.password}")
  private String password;

  private static final String REDISSON_HOST_PREFIX = "redis://";

//  @Bean
//  public RedisConnectionFactory redisConnectionFactory() {
//    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
//    redisStandaloneConfiguration.setHostName(host);
//    redisStandaloneConfiguration.setPort(port);
//    redisStandaloneConfiguration.setPassword(password);
//    return new LettuceConnectionFactory(redisStandaloneConfiguration);
//  }

  @Bean
  public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
    return new RedissonConnectionFactory(redisson);
  }

  @Bean
  public RedissonClient redissonClient() {
    RedissonClient redisson = null;
    Config config = new Config();
    config.useSingleServer().setAddress(REDISSON_HOST_PREFIX + host + ":" + port);
    redisson = Redisson.create(config);
    return redisson;
  }

}
