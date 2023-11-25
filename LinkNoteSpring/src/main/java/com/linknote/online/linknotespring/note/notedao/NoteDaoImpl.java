package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class NoteDaoImpl implements NoteDao{
  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public Integer createNote(CreateNoteParamsDto params, Integer notebookId) {
    String sql = "INSERT INTO notes (name, notebookId) VALUES (:noteName, :notebookId)";
    Map<String, Object> map = new HashMap<>();
    map.put("noteName", params.getNoteName());
    map.put("notebookId", notebookId);
    KeyHolder keyHolder = new GeneratedKeyHolder();
    namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);

    return Objects.requireNonNull(keyHolder.getKey()).intValue();
  }

  @Override
  public Integer updateNote(UpdateNoteParamsDto params) {
    Map<String , Object> map = new HashMap<>();
    String sql = "UPDATE notes SET ";
    if(!params.getContent().isEmpty()){
      sql += "content = :content, ";
      map.put("content", params.getContent());
    }
    if(!params.getQuestion().isEmpty()){
      sql += "question = :question, ";
      map.put("question", params.getQuestion());
    }
    if(!params.getKeypoint().isEmpty()){
      sql += "keypoint = :keypoint, ";
      map.put("keypoint", params.getKeypoint());
    }
    if(params.getSharedPermission() != null){
     sql += "sharedPermission = :sharedPermission, ";
      map.put("sharedPermission", params.getSharedPermission());
    }
    if(params.getStar() != null){
      sql += "star = :star ";
      map.put("star", params.getStar());
    }
    sql += "Where notebookId = :notebookId";
    map.put("notebookId", params.getNotebookId());
    return namedParameterJdbcTemplate.update(sql, map);
  }
}
