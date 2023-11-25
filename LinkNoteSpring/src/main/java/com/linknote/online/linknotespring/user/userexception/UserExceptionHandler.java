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

//  @ExceptionHandler(RuntimeException.class)
//  public ResponseEntity<Object> runTimeException(RuntimeException e){
//    log.warn("RunTime Exception. 非預期錯誤，錯誤訊息：" + e.getMessage());
//    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("msg", "Internal error", "result", false));
//  }

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

  @ExceptionHandler(TokenParseException.class)
  public ResponseEntity<Object> tokenParseException(TokenParseException e){
    log.warn("token解析失敗");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("msg", e.getMessage(),"result", false));
  }

  @ExceptionHandler(EmailAlreadyDisabledException.class)
  public ResponseEntity<Object> emailAlreadyDisabledException(EmailAlreadyDisabledException e){
    log.warn("Email already disabled");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("msg", e.getMessage(), "result", false));
  }

  @ExceptionHandler(TokenExpirationException.class)
  public ResponseEntity<Object> tokenExpirationException(TokenExpirationException e){
    log.warn("Token 已經過期.");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("msg", e.getMessage(), "result", false));
  }
  @ExceptionHandler(TokenInvalidException.class)
  public ResponseEntity<Object> tokenInvalidException(TokenInvalidException e){
    log.warn("無效的Token，DB尚未找到資料。");
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("msg", e.getMessage(), "result", false));
  }
}
