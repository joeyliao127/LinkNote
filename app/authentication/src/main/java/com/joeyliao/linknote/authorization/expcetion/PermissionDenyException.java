package com.joeyliao.linknote.authorization.expcetion;

public class PermissionDenyException extends RuntimeException {

  public PermissionDenyException(String message) {
    super(message);
  }
}
