package com.joeyliao.linknote.generic.exception;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GenericExceptionHandler {

  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public ResponseEntity<Object> sqlDemonstrabilityViolationHandler(SQLIntegrityConstraintViolationException e){
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("result", false, "msg", "重複的資料"));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> badRequestException(BadRequestException e){
    return ResponseEntity.status(400).body(Map.of("result", false, "msg", e.getMessage()));
  }
}
