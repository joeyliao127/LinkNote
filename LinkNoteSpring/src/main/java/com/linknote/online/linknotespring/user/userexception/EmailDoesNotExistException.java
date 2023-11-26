package com.linknote.online.linknotespring.user.userexception;

public class EmailDoesNotExistException extends RuntimeException{

  public EmailDoesNotExistException(String message) {
    super(message);
  }
}
