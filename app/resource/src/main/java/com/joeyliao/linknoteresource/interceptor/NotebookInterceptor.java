package com.joeyliao.linknoteresource.interceptor;

import com.joeyliao.linknoteresource.enums.generic.Target;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class NotebookInterceptor implements HandlerInterceptor {

  @Autowired
  AuthorizationHandler authorizationHandler;
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    if(Objects.equals(request.getMethod(), "OPTIONS")){
      return true;
    }
    Boolean verifyResult = authorizationHandler.checkAccessPermission(request, response, Target.NOTEBOOK);
    log.info("Notebook驗證結果為：" + verifyResult);
    return verifyResult;
  }
}
