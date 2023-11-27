package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.noterowmapper.TagRowMapper;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TagDaoImpl implements TagDao{


  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public String verifyTagExist(Integer notebookId, String tag) {
    String sql = "SELECT name FROM tags WHERE name = :tag AND notebookId = :notebookId";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    map.put("tag", tag);
    List<String> result = namedParameterJdbcTemplate.query(sql, map, new RowMapper<String>() {
      @Override
      public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("name");
      }
    });
    if(result.isEmpty()){
      return null;
    }else{
      return result.get(0);
    }
  }

  @Override
  public void createNotebookTag(String tag, Integer notebookId) {
    String sql = "INSERT INTO tags (name, notebookId) VALUE(:tag, :notebookId)";
    Map<String, Object> map = new HashMap<>();
    map.put("tag", tag);
    map.put("notebookId", notebookId);
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void createNotebookTags(List<String> tagList, Integer notebookId) {
    String sql = "INSERT INTO tags (name, notebookId) VALUE(:tag, :notebookId)";
    MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[tagList.size()];
    for(int i=0; i< tagList.size(); i++){
      parameterSources[i] = new MapSqlParameterSource();
      parameterSources[i].addValue("tag", tagList.get(i));
      parameterSources[i].addValue("notebooktId", notebookId);
    };
    namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
  }

  @Override
  public void createNoteTag(CreateNoteTagParamDto param) {
    String sql = "UPDATE tags SET noteId = :noteId "
        + "WHERE notebookId = :notebookId AND name = :tag";
    Map<String, Object> map = new HashMap<>();
    map.put("noteId", param.getNoteId());
    map.put("notebookId", param.getNotebookId());
    map.put("tag", param.getTag());
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void deleteNoteTag(DeleteNoteParamDto param) {
    String sql = "DELETE FROM tags "
        + "WHERE noteId = :noteId AND name = :tag AND notebookId = :notebookId";
    Map<String, Object> map = new HashMap<>();
    map.put("noteId", param.getNoteId());
    map.put("notebookId", param.getNotebookId());
    map.put("tag", param.getTag());
    namedParameterJdbcTemplate.update(sql, map);
  }
}
