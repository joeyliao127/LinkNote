package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NoteDaoImpl implements NoteDao{
  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public Integer getNoteIdByNameForVerifyNameExist(String name, Integer notebookId) {
    String sql = "SELECT id FROM notes WHERE notebookId = :notebookId AND name = :name";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    map.put("name", name);
    List<Integer> result = namedParameterJdbcTemplate.query(sql, map, new RowMapper<Integer>() {
      @Override
      public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("id");
      }
    });
    if(result.isEmpty()){
      return null;
    }else {
      return result.get(0);
    }
  }

  @Override
  public void createNote(CreateNoteParamsDto params) {
    String sql = "INSERT INTO notes (name, notebookId) VALUES (:noteName, :notebookId)";
    Map<String, Object> map = new HashMap<>();
    map.put("noteName", params.getNoteName());
    map.put("notebookId", params.getNotebookId());
    namedParameterJdbcTemplate.update(sql,map);
  }

  @Override
  public void updateNote(UpdateNoteParamsDto params) {
    Map<String , Object> map = new HashMap<>();
    String sql = "UPDATE notes SET ";

    if(!params.getContent().isEmpty() &&
        !Objects.equals(params.getContent(), " ")){
        sql += "content = :content, ";
        map.put("content", params.getContent());
    }else if(Objects.equals(params.getContent(), " ")){
      sql += "content = ' ', ";
    }

    if(!params.getQuestion().isEmpty() &&
        !Objects.equals(params.getQuestion(), " ")){
      sql += "question = :question, ";
      map.put("question", params.getQuestion());
    }else if(Objects.equals(params.getQuestion(), " ")){
      sql += "question = ' ', ";
    }

    if(!params.getKeypoint().isEmpty() &&
        !Objects.equals(params.getKeypoint(), " ")){
      sql += "keypoint = :keypoint, ";
      map.put("keypoint", params.getKeypoint());
    }else if(Objects.equals(params.getKeypoint(), " ")){
      sql += "keypoint = ' ', ";
    }

    if(params.getSharedPermission() != null){
     sql += "sharedPermission = :sharedPermission, ";
      map.put("sharedPermission", params.getSharedPermission());
    }

    sql += "Where notebookId = :notebookId";
    map.put("notebookId", params.getNotebookId());
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void updateNoteStar(UpdateNoteStarParamDto params) {
    String sql = "UPDATE notes SET star = :star WHERE notebookId = :notebookId AND id = :noteId";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", params.getNotebookId());
    map.put("noteId", params.getNoteId());
    map.put("star", params.isStar());
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void deleteNote(DeleteNoteParamDto param) {
    String sql = "DELETE FROM notes WHERE id = :noteId AND notebookId = :notebookId";
    Map<String, Object> map = new HashMap<>();
    map.put("noteId", param.getNoteId());
    map.put("notebookId", param.getNotebookId());
    namedParameterJdbcTemplate.update(sql, map);
  }
}
