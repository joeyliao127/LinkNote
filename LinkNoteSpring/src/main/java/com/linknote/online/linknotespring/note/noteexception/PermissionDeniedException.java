package com.linknote.online.linknotespring.note.noteexception;

public class PermissionDeniedException extends RuntimeException{

  public PermissionDeniedException(String message) {
    super(message);
  }
}
