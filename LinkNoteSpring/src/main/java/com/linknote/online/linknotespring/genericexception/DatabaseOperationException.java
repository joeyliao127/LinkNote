package com.linknote.online.linknotespring.genericexception;

public class  DatabaseOperationException extends RuntimeException{
    public DatabaseOperationException(String message, Throwable exp ){
      super(message, exp);
    }
}
