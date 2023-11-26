package com.linknote.online.linknotespring.generic.exception;

import com.linknote.online.linknotespring.note.noteexception.NoteExceptionHandler;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {

  private final static Logger log = LoggerFactory.getLogger(NoteExceptionHandler.class);
//  @org.springframework.web.bind.annotation.ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//  public ResponseEntity<Object> sqlInegerityConstraintViolationHanlder(SQLIntegrityConstraintViolationException e){
//    log.warn("generic exception handler: 資料庫新增或更新資料錯誤" + e.getMessage());
//    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//        .body(Map.of("result", false, "msg", "新增或更新資料錯誤"));
//  }
}
