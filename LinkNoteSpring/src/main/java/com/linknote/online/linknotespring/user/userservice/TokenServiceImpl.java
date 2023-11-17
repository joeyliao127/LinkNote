package com.linknote.online.linknotespring.user.userservice;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{

  @Value("${jwt.secretKey}")
  private String SECRET;

  @Override
  public String genJWTToken(Integer userId, String email, String username) {
    System.out.println("Secret key = " + SECRET);
    byte[] decodekey = Base64.getDecoder().decode(SECRET.getBytes(StandardCharsets.UTF_8));
    return Jwts.builder()
          .issuedAt(new Date())
          .issuer("linkNote")
          .expiration(new Date(System.currentTimeMillis() + 604800000))
          .subject(username)
          .claim("userId", userId)
          .claim("email", email)
          .signWith(new SecretKeySpec(decodekey,"HmacSHA256"))
          .compact();
  }

  @Override
  public Claims parserJWTToken(String token) {
    byte[] decodekey = Base64.getDecoder().decode(SECRET.getBytes(StandardCharsets.UTF_8));
    SecretKey key = new SecretKeySpec(decodekey, "HmacSHA256");
    Claims payload = Jwts.parser().verifyWith(key).build().parseClaimsJws(token).getPayload();
    System.out.println("exp:" +payload.getExpiration());
    System.out.println("issuer" + payload.getIssuer());
    System.out.println("claims" + payload.get("username"));
    return payload;
  }
}
