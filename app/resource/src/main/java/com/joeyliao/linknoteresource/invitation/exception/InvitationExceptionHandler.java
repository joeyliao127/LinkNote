package com.joeyliao.linknoteresource.invitation.exception;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class InvitationExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<Object> invitationAlreadyExistExceptionHandler(
      InvitationAlreadyExistException e
  ) {
    log.info("==========InvitationExceptionHandler==========");
    log.warn(e.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT)
        .body(Map.of("result", false, "msg", "重複的資料"));
  }
}
