package com.linknote.online.linknotespring.note.notedao;

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
public class IntermediaryDaoImpl implements IntermediaryDao {

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Override
  public Integer updateNotebookTags(Integer notebookId, Integer tagId) {
    String sql = "INSERT INTO notebooks_tags (notebookId, tagId)"
        + " VALUES (:notebookId, :tagId)";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    map.put("tagId", tagId);
    return namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public Integer createNotTags(Integer tagId, Integer noteId) {
    String sql = "INSERT INTO notes_tags (noteId, tagId) VALUES(:noteId, :tagId)";
    Map<String, Object> map = new HashMap<>();
    map.put("noteId", noteId);
    map.put("tagId", tagId);
    return namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public Integer getNoteTagPair(Integer noteId, Integer tagId) {
    String sql = "SELECT tagId FROM notes_tags WHERE noteId = :noteId AND tagId = :tagId";
    Map<String, Object> map = new HashMap<>();
    map.put("noteId", noteId);
    map.put("tagId", tagId);
    List<Integer> result = namedParameterJdbcTemplate.query(sql, map, new RowMapper<Integer>() {
      @Override
      public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("tagId");
      }
    });
    if(result.isEmpty()){
      return null;
    }else {
      return result.get(0);
    }
  }
}
