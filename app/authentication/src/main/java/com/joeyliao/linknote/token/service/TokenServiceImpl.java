package com.joeyliao.linknote.token.service;

import com.joeyliao.linknote.token.exception.TokenExpirationException;
import com.joeyliao.linknote.token.exception.TokenInvalidException;
import com.joeyliao.linknote.user.dao.UserDAO;
import com.joeyliao.linknote.user.po.UserInfoPO;
import com.joeyliao.linknote.user.service.UserServiceImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import javax.crypto.BadPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{

  @Value("${jwt.secretKey}")
  private String SECRET;

  @Autowired
  UserDAO userDAO;

  private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
  @Override
  public String genJWTToken(String userId, String email, String username) {
    byte[] decodekey = Base64.getDecoder().decode(SECRET.getBytes(StandardCharsets.UTF_8));
    return Jwts.builder()
          .issuedAt(new Date())
          .issuer("linkNote")
          .expiration(new Date(System.currentTimeMillis() + 604800000L * 4))//7 days
          .subject(username)
          .claim("userId", userId)
          .claim("email", email)
          .signWith(new SecretKeySpec(decodekey,"HmacSHA256"))
          .compact();
  }

  @Override
  public Claims parserJWTToken(String Authorization) {
    try {
      if(Authorization == null){
        throw new BadRequestException("Token is null");
      }
    }catch (BadRequestException e){
      log.info(e.getMessage());
    }
    String token = Authorization.substring(7);
    byte[] decodekey = Base64.getDecoder().decode(SECRET.getBytes(StandardCharsets.UTF_8));
    SecretKey key = new SecretKeySpec(decodekey, "HmacSHA256");
    try{
      Claims payload = Jwts.parser().verifyWith(key).build().parseClaimsJws(token).getPayload();
      return payload;
    }catch (RuntimeException e){
      log.warn("token解析錯誤：" + e.getMessage());
      e.getMessage();
      throw new TokenInvalidException("invalid token");
    }
  }

  @Override
  public Boolean verifyToken(String Authorization) {
    Claims claims = this.parserJWTToken(Authorization);
    if(System.currentTimeMillis() < claims.getExpiration().getTime()){
      List<UserInfoPO> userInfoPOS = userDAO.getByTokenUserIdAndEmailForToken(
          claims.get("email", String.class),
          claims.get("userId", String.class));
      if(userInfoPOS.isEmpty()){
        log.warn("token Service: 無效的token.");
        throw new TokenInvalidException("Invalid token");
      }else{
        log.info("TokenService - JWT token驗證成功，允許使用者登入。 User: " + claims.getSubject() + " UserId: " + claims.get("userId", String.class));
        return true;
      }
    }else{
      log.warn("token已過期. (TokenImpl)");
      throw new TokenExpirationException("token expired");
    }
  }
}
