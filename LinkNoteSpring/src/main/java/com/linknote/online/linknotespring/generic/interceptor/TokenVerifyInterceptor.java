package com.linknote.online.linknotespring.generic.interceptor;

import com.linknote.online.linknotespring.user.userexception.TokenInvalidException;
import com.linknote.online.linknotespring.user.userservice.TokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TokenVerifyInterceptor implements HandlerInterceptor {
  private final static Logger log = LoggerFactory.getLogger(TokenVerifyInterceptor.class);

  @Autowired
  private TokenService tokenService;
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    try {
      Cookie[] cookies = request.getCookies();
      log.info("檢查cookie是否為空");
      if(cookies != null){
        for(Cookie cookie : cookies){
          log.info(cookie.getValue());
          log.info("攔截器接收到cookie：取得/驗證token.");
          return tokenService.verifyToken(cookie.getValue());
        }
      }
      throw new TokenInvalidException("Token not found");
    }catch (RuntimeException e){
      log.warn("Token攔截器：無效的token，錯誤訊息。\n " + e);
      throw new TokenInvalidException("invalid token");
    }
  }
}
