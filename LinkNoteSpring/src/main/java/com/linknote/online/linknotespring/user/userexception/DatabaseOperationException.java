package com.linknote.online.linknotespring.user.userexception;

public class DatabaseOperationException extends RuntimeException{
    public DatabaseOperationException(String message, Throwable exp ){
      super(message, exp);
    }
}
