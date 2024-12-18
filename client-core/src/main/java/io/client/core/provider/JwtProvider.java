package io.client.core.provider;

import io.client.core.entity.RefreshToken;
import io.client.core.enums.JwtCode;
import io.client.core.exception.enums.ClientCoreExceptionEnum;
import io.client.core.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.system.core.exception.ApiException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtProvider {
  @Value("${jwt.secret}")
  private String secretKey;

  private Key key;

  private final String AUTHORITIES_KEY = "AUTH";

  @Value("${jwt.accessTokenExpiredTime}")
  private long accessTokenExpiredTime;
  @Value("${jwt.refreshTokenExpiredTime}")
  private long refreshTokenExpiredTime;

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserDetailsService userDetailsService;

  @PostConstruct
  protected void init() {
    // secret key encode base64
    String encodedKey = Base64.getEncoder()
        .encodeToString(secretKey.getBytes());
    key = Keys.hmacShaKeyFor(encodedKey.getBytes());
  }

  // token 생성
  public String generateToken(String userId, List<String> authorities, Long accessTokenExpiredTime) {

    // access token 만료시간
    Date now = new Date();
    Date expiration = new Date(now.getTime() + accessTokenExpiredTime);

    return Jwts.builder()
        .setSubject(userId)
        .claim(AUTHORITIES_KEY, authorities)
        .setIssuedAt(now) // 발행시간
        .setExpiration(expiration)  // 만료시간
        .signWith(key, SignatureAlgorithm.HS512)  // (비밀키, 해싱 알고리즘)
        .compact();
  }

  // access token 발급
  public String generateAccessToken(String userId, List<String> authorities) {
    return this.generateToken(userId, authorities, accessTokenExpiredTime);
  }

  // refresh token 발급
  public String generateRefreshToken(String userId, List<String> authorities) {
    return generateToken(userId, authorities, refreshTokenExpiredTime);
  }

  public Authentication getAuthentication(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();

    Collection<? extends GrantedAuthority> authorities =
        Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
            .map(SimpleGrantedAuthority::new)
            .toList();

    UserDetails principal = userDetailsService.loadUserByUsername(claims.getSubject());
    return new UsernamePasswordAuthenticationToken(principal, "", null);
  }

  public JwtCode validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
      return JwtCode.ACCESS;
    } catch (ExpiredJwtException e) {
      // 기한만료
      return JwtCode.EXPIRED;
    } catch (Exception e) {
      return JwtCode.DENIED;
    }
  }

  public RefreshToken getRefreshToken(String userId) {

    return refreshTokenRepository.findById(userId)
        .orElseThrow(() -> new ApiException(ClientCoreExceptionEnum.EXPIRED_REFRESH_TOKEN));
  }

  public String getSubject(String token) {
    Claims claims = Jwts.parserBuilder()
        .setSigningKey(key)
        .build()
        .parseClaimsJws(token)
        .getBody();

    return claims.getSubject();
  }

}
