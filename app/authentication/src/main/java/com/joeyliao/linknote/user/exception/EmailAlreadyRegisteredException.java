package com.joeyliao.linknote.user.exception;

public class EmailAlreadyRegisteredException extends RuntimeException{
    public EmailAlreadyRegisteredException(String message){
      super(message);
    }
}
