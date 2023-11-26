package com.linknote.online.linknotespring.note.noteexception;

public class CollaboratorsAreLimitException extends RuntimeException{

  public CollaboratorsAreLimitException(String message) {
    super(message);
  }
}
