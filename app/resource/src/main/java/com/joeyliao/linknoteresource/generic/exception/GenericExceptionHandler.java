package com.joeyliao.linknoteresource.generic.exception;

import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GenericExceptionHandler {


  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public ResponseEntity<Object> sqlInegerityConstraintViolationHanlder(SQLIntegrityConstraintViolationException e){

    log.info(e.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("result", false, "msg", "重複的資料"));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Object> badRequestHandler(BadRequestException e){
    return ResponseEntity.status(400).body(Map.of("result", false, "msg", e));
  }


}
