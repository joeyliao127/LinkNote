package com.linknote.online.linknotespring.note.noteexception;

public class NoteAlreadyExistException extends RuntimeException{

  public NoteAlreadyExistException(String message) {
    super(message);
  }
}
