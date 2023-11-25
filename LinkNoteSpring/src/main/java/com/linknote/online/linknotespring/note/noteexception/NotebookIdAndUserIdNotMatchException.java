package com.linknote.online.linknotespring.note.noteexception;

public class NotebookIdAndUserIdNotMatchException extends RuntimeException{

  public NotebookIdAndUserIdNotMatchException(String message) {
    super(message);
  }
}
