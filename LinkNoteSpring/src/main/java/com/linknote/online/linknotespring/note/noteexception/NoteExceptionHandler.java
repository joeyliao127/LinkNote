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
    log.warn("NoteExceptionHandler: notebookId And UserId Not Match Exception");
    log.info(e.getMessage());
    return ResponseEntity.status(401).body(Map.of("result", false, "msg", e.getMessage()));
  }

  @ExceptionHandler(NotebookAlreadyExistsException.class)
  public ResponseEntity<Object> notebookAlreadyExistsException(NotebookAlreadyExistsException e){
    log.warn("NoteExceptionHandler: notebookAlreadyExistsException");
    log.info(e.getMessage());
    return ResponseEntity.status(400).body(Map.of("result", false, "msg", "筆記本名稱重複"));
  }

  @ExceptionHandler(CollaboratorsAreLimitException.class)
  public ResponseEntity<Object> collaboratorsAreLimitException(CollaboratorsAreLimitException e){
    log.warn("NoteExceptionHandler: CollaboratorsAreLimitException");
    log.warn(e.getMessage());
    return ResponseEntity.status(400).body(Map.of("result", false, "msg", "超過共編人數上限(最多四人)"));
  }



}
