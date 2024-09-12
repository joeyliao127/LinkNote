package com.joeyliao.linknoteresource.coedit;

import com.joeyliao.linknoteresource.dao.NoteDAO;
import com.joeyliao.linknoteresource.dto.note.NoteDTO;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

// Singleton
@Component
@Slf4j
public class NoteContainer {

  private final NoteDAO noteDAO;
  private final Map<String, Map<String, Object>> container = new ConcurrentHashMap<>();

  public NoteContainer(NoteDAO noteDAO) {
    this.noteDAO = noteDAO;
  }

  public String getNoteContent(String noteId) {
    if(this.container.containsKey(noteId)) {
      return this.container.get(noteId).get("content").toString();
    } else {
      NoteDTO noteDTO = this.noteDAO.getNote(noteId);
      Map<String, Object> map = new HashMap<>();
      map.put("version", 1);
      map.put("content", noteDTO.getContent());
      this.container.put(noteId, map);
      return noteDTO.getContent();
    }
  }
}
