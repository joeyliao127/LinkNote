package com.joeyliao.linknote.authorization.expcetion;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class PermissionExceptionHandler {

  @ExceptionHandler(PermissionDenyException.class)
  public ResponseEntity<Object> permissionDenyException(PermissionDenyException e){
    log.info("Permission Exception Handler: Permission Deny Exception");
    log.info(e.getMessage());
    return ResponseEntity.status(403).body(Map.of("result", false,"msg", "permission deny"));
  }
}
