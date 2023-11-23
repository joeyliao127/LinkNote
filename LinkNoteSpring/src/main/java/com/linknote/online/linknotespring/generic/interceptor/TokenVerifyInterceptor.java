package com.linknote.online.linknotespring.generic.interceptor;

import com.linknote.online.linknotespring.user.userexception.TokenInvalidException;
import com.linknote.online.linknotespring.user.userservice.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
      String token = request.getHeader("Authorization").substring(7);
      log.info("攔截器：驗證token.");
      return tokenService.verifyToken(token);
    }catch (RuntimeException e){
      log.warn("Token攔截器：無效的token，錯誤訊息。\n " + e);
      throw new TokenInvalidException("未檢測到token");
    }
  }
}
