package com.linknote.online.linknotespring.note.noteexception;

public class NotebookDoesNotExistException extends RuntimeException{

  public NotebookDoesNotExistException(String message) {
    super(message);
  }
}
