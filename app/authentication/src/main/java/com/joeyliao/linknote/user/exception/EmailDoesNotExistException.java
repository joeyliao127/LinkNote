package com.joeyliao.linknote.user.exception;

public class EmailDoesNotExistException extends RuntimeException{

  public EmailDoesNotExistException(String message) {
    super(message);
  }
}
