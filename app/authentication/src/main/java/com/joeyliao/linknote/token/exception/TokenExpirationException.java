package com.joeyliao.linknote.token.exception;

public class TokenExpirationException extends RuntimeException{

  public TokenExpirationException(String message) {
    super(message);
  }
}