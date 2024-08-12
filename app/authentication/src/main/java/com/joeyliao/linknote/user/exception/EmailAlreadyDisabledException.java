package com.joeyliao.linknote.user.exception;

public class EmailAlreadyDisabledException extends RuntimeException{

  public EmailAlreadyDisabledException(String message) {
    super(message);
  }

  public EmailAlreadyDisabledException(String message, Throwable cause) {
    super(message, cause);
  }

  public EmailAlreadyDisabledException(Throwable cause) {
    super(cause);
  }
}
