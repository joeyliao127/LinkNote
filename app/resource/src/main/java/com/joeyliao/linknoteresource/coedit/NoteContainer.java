package com.joeyliao.linknoteresource.coedit;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

// Singleton
@Component
public class NoteContainer {

  private final Map<String, String> container = new ConcurrentHashMap<>();

  public void appendNote(String noteId, String content) {
    this.container.put(noteId, content);
  }

  public String getNoteContent(String noteId) {
    return this.container.get(noteId);
  }
}
