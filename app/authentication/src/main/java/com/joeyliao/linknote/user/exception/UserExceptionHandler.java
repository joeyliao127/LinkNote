package com.joeyliao.linknote.user.exception;

import com.joeyliao.linknote.generic.exception.DatabaseOperationException;
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

  @ExceptionHandler(EmailDoesNotExistException.class)
  public ResponseEntity<Object> emailDoesNotExistException(EmailDoesNotExistException e){
    log.warn("UserExceptionHandler: EmailDoesNotExistException");
    log.warn(e.getMessage());
    return ResponseEntity.status(400).body(Map.of("result", false, "msg", "email doesn't exist"));
  }

  @ExceptionHandler(EmailAlreadyRegisteredException.class)
  public ResponseEntity<Object> emailAlreadyRegisterdHandler(EmailAlreadyRegisteredException e){
    log.warn("Email已經被註冊");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        Map.of("message", "email already exist", "result", false));
  }

  @ExceptionHandler(DatabaseOperationException.class)
  public ResponseEntity<Object> databaseOperationHandler(DatabaseOperationException e){
    log.warn("資料庫異常");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of( "message", e.getMessage(),"result", false));
  }

  @ExceptionHandler(VerifyUserFailedException.class)
  public ResponseEntity<Object> emailNotExistHandler(VerifyUserFailedException e){
    log.warn("使用者帳號或密碼錯誤");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("msg", e.getMessage(),"result", false));
  }


  @ExceptionHandler(EmailAlreadyDisabledException.class)
  public ResponseEntity<Object> emailAlreadyDisabledException(EmailAlreadyDisabledException e){
    log.warn("Email already disabled");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("msg", e.getMessage(), "result", false));
  }

}
