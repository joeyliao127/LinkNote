package com.linknote.online.linknotespring.user.userexception;

public class TokenExpirationException extends RuntimeException{

  public TokenExpirationException(String message) {
    super(message);
  }
}
