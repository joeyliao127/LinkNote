package com.linknote.online.linknotespring.user.userexception;

public class TokenInvalidException extends RuntimeException{

  public TokenInvalidException(String message) {
    super(message);
  }
}
