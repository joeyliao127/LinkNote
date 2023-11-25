package com.linknote.online.linknotespring.user.userservice;
import com.linknote.online.linknotespring.user.userdao.UserDAO;
import com.linknote.online.linknotespring.user.userexception.TokenExpirationException;
import com.linknote.online.linknotespring.user.userexception.TokenInvalidException;
import com.linknote.online.linknotespring.user.userexception.TokenParseException;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Base64;
import java.util.List;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService{

  @Value("${jwt.secretKey}")
  private String SECRET;

  @Autowired
  UserDAO userDAO;

  private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
  @Override
  public String genJWTToken(Integer userId, String email, String username) {
    byte[] decodekey = Base64.getDecoder().decode(SECRET.getBytes(StandardCharsets.UTF_8));
    return Jwts.builder()
          .issuedAt(new Date())
          .issuer("linkNote")
          .expiration(new Date(System.currentTimeMillis() + 604800000))//7 days
          .subject(username)
          .claim("userId", userId)
          .claim("email", email)
          .signWith(new SecretKeySpec(decodekey,"HmacSHA256"))
          .compact();
  }

  @Override
  public Claims parserJWTToken(String Authorization) {
    log.info("Token service: 接收到前端的Authorization，內容\n" + Authorization);
    String token = Authorization.substring(7);
    byte[] decodekey = Base64.getDecoder().decode(SECRET.getBytes(StandardCharsets.UTF_8));
    SecretKey key = new SecretKeySpec(decodekey, "HmacSHA256");
    try{
      Claims payload = Jwts.parser().verifyWith(key).build().parseClaimsJws(token).getPayload();
      return payload;
    }catch (RuntimeException e){
      log.warn("token解析錯誤：" + e.getMessage());
      e.getMessage();
      throw new TokenParseException("Token解析錯誤：");
    }
  }

  @Override
  public Boolean verifyToken(String Authorization) {
    Claims claims = this.parserJWTToken(Authorization);
    if(System.currentTimeMillis() < claims.getExpiration().getTime()){
      List<UserInfoPO> userInfoPOS = userDAO.getByTokenUserIdAndEmailForToken(
          claims.get("email", String.class),
          claims.get("userId", Integer.class));
      if(userInfoPOS.isEmpty()){
        log.warn("無效的token. (TokenImpl)");
        throw new TokenInvalidException("Invalid token");
      }else{
        log.info("TokenService - JWT token驗證成功，允許使用者登入。 User: " + claims.getSubject() + " UserId: " + claims.get("userId", Integer.class));
        return true;
      }
    }else{
      log.warn("token已過期. (TokenImpl)");
      throw new TokenExpirationException("token expired");
    }
  }
}
