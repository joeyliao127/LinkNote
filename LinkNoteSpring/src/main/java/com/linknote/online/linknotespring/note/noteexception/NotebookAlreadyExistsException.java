package com.linknote.online.linknotespring.note.noteexception;

public class NotebookAlreadyExistsException extends RuntimeException{

  public NotebookAlreadyExistsException(String message) {
    super(message);
  }
}
