package com.linknote.online.linknotespring.note.noteexception;

public class TagNotFoundException extends RuntimeException{

  public TagNotFoundException(String message) {
    super(message);
  }
}
