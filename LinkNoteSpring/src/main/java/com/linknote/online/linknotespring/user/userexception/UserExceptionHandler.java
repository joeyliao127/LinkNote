package com.linknote.online.linknotespring.user.userexception;

import com.linknote.online.linknotespring.genericexception.DatabaseOperationException;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class UserExceptionHandler {
  @ExceptionHandler(EmailAlreadyRegisteredException.class)
  public ResponseEntity<Object> emailAlreadyRegisterdHandler(EmailAlreadyRegisteredException e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        Map.of("message", "email already exist"));
  }

  @ExceptionHandler(DatabaseOperationException.class)
  public ResponseEntity<Object> databaseOperationHandler(DatabaseOperationException e){
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of( "message", e.getMessage()));
  }

}
