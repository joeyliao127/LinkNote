package com.linknote.online.linknotespring.user.userexception;

public class EmailAlreadyRegisteredException extends RuntimeException{
    public EmailAlreadyRegisteredException(String message){

      super(message);

    }
}
