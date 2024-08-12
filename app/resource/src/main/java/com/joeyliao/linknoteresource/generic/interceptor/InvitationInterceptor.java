package com.joeyliao.linknoteresource.generic.interceptor;

import com.joeyliao.linknoteresource.generic.enums.Target;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class InvitationInterceptor implements HandlerInterceptor {

  @Autowired
  AuthorizationHandler authorizationHandler;
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if(Objects.equals(request.getMethod(), "OPTIONS")){
      return true;
    }
    return authorizationHandler.checkAccessPermission(request,response, Target.INVITATION);
  }
}
