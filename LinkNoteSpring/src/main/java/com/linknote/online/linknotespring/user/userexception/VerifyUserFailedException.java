package com.linknote.online.linknotespring.user.userexception;

public class VerifyUserFailedException extends RuntimeException{
  public VerifyUserFailedException(String message){
    super(message);
  }
}
