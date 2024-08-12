package com.joeyliao.linknote.authorization.controller;

import com.joeyliao.linknote.authorization.enums.Target;
import com.joeyliao.linknote.authorization.requestobject.PermissionRequest;
import com.joeyliao.linknote.token.service.TokenService;
import io.jsonwebtoken.Claims;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PermissionRequestObjectHandler {
  @Autowired
  TokenService tokenService;
  public void setRequestAttribute(
      PermissionRequest request,
      String Authorization,
      Target target
  ) {
    request.setUserId(setUserId(Authorization));
    request.setTarget(target);
  }

  private String setUserId(String Authorization) {
    log.info("Token有沒有：" + Authorization);

    if (!(Objects.equals(Authorization, ""))) {
      Claims claims = tokenService.parserJWTToken(Authorization);
      return claims.get("userId", String.class);
    } else {
      return null;
    }
  }
}
