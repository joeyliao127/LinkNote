package com.joeyliao.linknote.token.exception;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

@ControllerAdvice
@Slf4j
public class TokenExceptionHandler {

  @ExceptionHandler(TokenExpirationException.class)
  public ResponseEntity<Object> tokenExpirationException(TokenExpirationException exception) {
    log.warn("Token Exception Handler: " + exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("result", false));
  }

  @ExceptionHandler(TokenInvalidException.class)
  public ResponseEntity<Object> tokenInvalidException(TokenInvalidException exception) {
    log.warn("Token Exception Handler: " + exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(Map.of("result", false, "msg", exception.getMessage()));
  }
}
