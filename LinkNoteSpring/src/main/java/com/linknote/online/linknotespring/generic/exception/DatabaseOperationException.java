package com.linknote.online.linknotespring.generic.exception;

import com.linknote.online.linknotespring.note.noteexception.NoteExceptionHandler;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class  DatabaseOperationException extends RuntimeException{

  public DatabaseOperationException(String message) {
    super(message);
  }
}
