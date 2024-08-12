package com.joeyliao.linknote.user.exception;

public class VerifyUserFailedException extends RuntimeException{
  public VerifyUserFailedException(String message){
    super(message);
  }
}
