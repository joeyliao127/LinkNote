package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NoteDaoImpl implements NoteDao{
  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public void createNote(CreateNoteParamsDto params, Integer notebookId) {
    String sql = "INSERT INTO notes (name, notebookId) VALUES (:noteName, :notebookId)";
    Map<String, Object> map = new HashMap<>();
    map.put("noteName", params.getNoteName());
    map.put("notebookId", notebookId);
    namedParameterJdbcTemplate.update(sql, map);
  }
}
