package com.linknote.online.linknotespring.note.noteexception;

import com.linknote.online.linknotespring.user.userexception.UserExceptionHandler;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NoteExceptionHandler {
  private final static Logger log = LoggerFactory.getLogger(NoteExceptionHandler.class);
  @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
  public ResponseEntity<Object> sqlInegerityConstraintViolationHanlder(SQLIntegrityConstraintViolationException e){
    log.warn("已經有此筆記本名稱");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("result", false, "msg", "已經有重複的名稱"));
  }

}
