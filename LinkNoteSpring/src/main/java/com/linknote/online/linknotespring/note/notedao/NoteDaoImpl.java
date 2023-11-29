package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotesParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteSharedParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;
import com.linknote.online.linknotespring.note.notepo.po.NotePO;
import com.linknote.online.linknotespring.note.notepo.po.NotesPO;
import com.linknote.online.linknotespring.note.noterowmapper.NoteRowMapper;
import com.linknote.online.linknotespring.note.noterowmapper.NotesRowMapper;
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
  public List<NotesPO> getNotes(GetNotesParamDto params) {
    Map<String, Object> map = new HashMap<>();
    String sql = "SELECT nt.id as noteId, nt.name, nt.question, nt.star, nt.createDate "
        + "FROM notes nt JOIN notebooks n ON notebookId = n.id ";
    if(!Objects.equals(params.getTag(), "null")){
      sql += "JOIN tags t ON nt.id = t.notebookId "
          + "WHERE nt.notebookId = :notebookId "
          + "AND n.userId = :userId AND t.name = :tag ";
      map.put("tag", params.getTag());
    }else{
      sql += "WHERE nt.notebookId = :notebookId AND n.userId = :userId ";
    }
    map.put("notebookId", params.getNotebookId());
    map.put("userId", params.getUserId());

    if(!Objects.equals(params.getKeyword(), "null")){
      sql += "AND nt.name like :keyword ";
      map.put("keyword", "%" + params.getKeyword() + "%");
      System.out.println("keyword = " + params.getKeyword());
    }

    if(params.getStar()){
      sql += "AND nt.star = :star ";
      map.put("star", params.getStar());
    }

    if(params.getTimeAsc()){
      sql += "ORDER BY createDate asc ";
    }

    sql += "LIMIT :limit OFFSET :offset ";
    map.put("limit", params.getLimit());
    map.put("offset", params.getOffset());

    System.out.println("最終拼完的sql:" + sql);
    return namedParameterJdbcTemplate.query(sql, map, new NotesRowMapper());
  }

  @Override
  public NotePO getNote(GetNoteParamDto param) {
    String sql = "SELECT nt.id as id, nt.name as name, nt.question as question, "
        + "nt.content as content, nt.keypoint as keypoint, "
        + "nt.sharedPermission as sharedPermission, "
        + "nt.star as star, nt.createDate as createDate"
        + " FROM notes nt "
        + "JOIN notebooks n ON n.id = nt.notebookId "
        + "WHERE nt.id = :noteId AND n.id = :notebookId";
    Map<String, Object> map = new HashMap<>();
    map.put("noteId", param.getNoteId());
    map.put("notebookId", param.getNotebookId());
    List<NotePO> result = namedParameterJdbcTemplate.query(sql, map, new NoteRowMapper());
    if(result.isEmpty()){
      return null;
    }else{
      return result.get(0);
    }
  }

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
      sql += "keypoint = :keypoint ";
      map.put("keypoint", params.getKeypoint());
    }else if(Objects.equals(params.getKeypoint(), " ")){
      sql += "keypoint = ' ', ";
    }

    sql += "WHERE notebookId = :notebookId";
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
  public void updateNoteShared(UpdateNoteSharedParamDto params) {
    String sql = "UPDATE notes SET sharedPermission = :star WHERE notebookId = :notebookId AND id = :noteId";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", params.getNotebookId());
    map.put("noteId", params.getNoteId());
    map.put("star", params.isShared());
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
