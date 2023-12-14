package com.linknote.online.linknotespring.user.userservice;

import static org.junit.jupiter.api.Assertions.*;

import com.linknote.online.linknotespring.user.userdao.UserDAO;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TokenServiceImplMockTest {

  @Autowired
  private TokenService tokenService;

  @Autowired
  UserDAO userDAO;

  @Test
  public void parserJWTToken(){
    String bearerToken = "Bearer eyJhbGciOiJIUzI1NiJ9"
        + ".eyJpYXQiOjE3MDIyOTkxNDEsImlzcyI6ImxpbmtOb3Rl"
        + "IiwiZXhwIjoxNzAyOTAzOTQxLCJzdWIiOiJKb2V5IExpYW"
        + "8iLCJ1c2VySWQiOjEsImVtYWlsIjoiam9leS5saWFvQGdtYWlsLmNvbSJ9"
        + ".WPxRV5p1IZ2x2fot7aCgMI92umqPmuwm1LsqbZMJPf0";

     Claims claims = tokenService.parserJWTToken(bearerToken);

    claims.get("email", String.class);
        claims.get("userId", Integer.class);
    assertEquals("joey.liao@gmail.com", claims.get("email", String.class));
    assertEquals(1, claims.get("userId", Integer.class));
  }

}