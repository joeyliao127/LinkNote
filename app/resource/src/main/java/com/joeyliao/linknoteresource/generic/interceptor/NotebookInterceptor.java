package com.joeyliao.linknoteresource.generic.interceptor;

import com.joeyliao.linknoteresource.generic.enums.Target;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

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
