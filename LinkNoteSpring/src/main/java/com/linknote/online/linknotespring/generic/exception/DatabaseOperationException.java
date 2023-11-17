package com.linknote.online.linknotespring.generic.exception;

public class  DatabaseOperationException extends RuntimeException{
    public DatabaseOperationException(String message, Throwable exp ){
      super(message, exp);
    }
}
