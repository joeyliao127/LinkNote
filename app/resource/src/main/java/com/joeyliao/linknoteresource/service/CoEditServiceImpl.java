package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.dao.NoteDAO;
import com.joeyliao.linknoteresource.dto.note.NoteDTO;
import com.joeyliao.linknoteresource.pojo.websocket.ReceivedOperationMessage;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CoEditServiceImpl implements CoEditService{
  private final NoteDAO noteDAO;
  private final Map<String, Map<String, Object>> noteContainer = new ConcurrentHashMap<>();
  @Autowired
  public CoEditServiceImpl(NoteDAO noteDAO) {
    this.noteDAO = noteDAO;
  }

  @Override
  public void getTransformOperation(ReceivedOperationMessage receivedOperationMessage) {

  }

  @Override
  public Map<String, Object> getNoteContentAndVersion(String noteId) {
    if(this.noteContainer.containsKey(noteId)) {
      String[] splitNoteContent = (String[]) this.noteContainer.get(noteId).get("noteContent");
      log.info("join轉字串：" + String.join("\n", splitNoteContent));
      Map<String, Object> map = new HashMap<>();
      map.put("noteContent",  String.join("\n", splitNoteContent));
      map.put("versionId", this.noteContainer.get(noteId).get("versionId").toString());
      return map;
    } else {
      NoteDTO noteDTO = this.noteDAO.getNote(noteId);
      String noteContent = noteDTO.getContent();
      String[] splitNoteContent = noteContent.split("\n");
      Map<String, Object> map = new HashMap<>();
      String versionId = UUID.randomUUID().toString().substring(24);
      map.put("versionId", versionId);
      map.put("noteContent", splitNoteContent);
      log.info("分割後的content");
      log.info(Arrays.toString(splitNoteContent));
      this.noteContainer.put(noteId, map);
      return Map.of("versionId", versionId, "noteContent", noteContent);
    }
  }

}
