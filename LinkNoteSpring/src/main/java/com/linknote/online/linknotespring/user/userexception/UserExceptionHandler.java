package com.linknote.online.linknotespring.user.userexception;

import com.linknote.online.linknotespring.generic.exception.DatabaseOperationException;
import com.linknote.online.linknotespring.user.userservice.UserServiceImpl;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {
  private final static Logger log = LoggerFactory.getLogger(UserExceptionHandler.class);
  @ExceptionHandler(EmailAlreadyRegisteredException.class)
  public ResponseEntity<Object> emailAlreadyRegisterdHandler(EmailAlreadyRegisteredException e){
    log.warn("Email已經被註冊");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        Map.of("message", "email already exist"));
  }

  @ExceptionHandler(DatabaseOperationException.class)
  public ResponseEntity<Object> databaseOperationHandler(DatabaseOperationException e){
    log.warn("資料庫異常");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of( "message", e.getMessage()));
  }

  @ExceptionHandler(VerifyUserFailedException.class)
  public ResponseEntity<Object> emailNotExistHandler(VerifyUserFailedException e){
    log.warn("使用者帳號或密碼錯誤");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("msg", e.getMessage()));
  }

}
