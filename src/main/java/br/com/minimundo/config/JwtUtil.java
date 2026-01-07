package br.com.minimundo.domain.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public final class JwtUtil {

  private static final String SECRET = "CHANGE_ME_TO_A_LONG_RANDOM_SECRET";
  private static final long EXP_MS = 1000L * 60 * 60; // 1h

  private JwtUtil() {}

  public static String generateToken(Long userId, String email) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
        .setSubject(String.valueOf(userId))
        .claim("email", email)
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + EXP_MS))
        .signWith(SignatureAlgorithm.HS256, SECRET)
        .compact();
  }

  public static Claims validate(String token) {
    return Jwts.parser()
        .setSigningKey(SECRET)
        .parseClaimsJws(token)
        .getBody();
  }
}
