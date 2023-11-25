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

  @ExceptionHandler(NotebookIdAndUserIdNotMatchException.class)
  public ResponseEntity<Object> notebookIdAndUserIdNotMatchException(NotebookIdAndUserIdNotMatchException e){
    log.warn("NoteExceptionHandler: notebookID and user id not match\nmsg:" + e);
    return ResponseEntity.status(400).body(Map.of("result", false, "msg", e.getMessage()));
  }


}
