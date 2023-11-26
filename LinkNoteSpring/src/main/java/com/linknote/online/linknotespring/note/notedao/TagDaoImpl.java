package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.noterowmapper.TagRowMapper;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TagDaoImpl implements TagDao{


  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Override
  public Integer createTag(String tag) {
    String sql = "INSERT INTO tags (name) VALUE(:tag)";
    Map<String, Object> map = new HashMap<>();
    map.put("tag", tag);
    namedParameterJdbcTemplate.update(sql, map);
    return null;
  }

  @Override
  public Integer getTagIdByTagNameAndNotebookId(Integer notebookId, String tag) {
    String sql = "SELECT t.id as tagId FROM tags t "
        + "JOIN notebooks_tags nt ON nt.tagId = t.id "
        + "JOIN notebooks n ON n.id = nt.notebookId "
        + "WHERE n.id = :notebookId AND t.name = :tag";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    map.put("tag", tag);
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

  @Override
  public Integer getTagIdByTagName(String tag) {
    String sql = "SELECT id FROM tags WHERE name = :tag";
    Map<String ,Object> map = new HashMap<>();
    map.put("tag", tag);
    List<Integer> tagId = namedParameterJdbcTemplate.query(sql, map, new RowMapper<Integer>() {
      @Override
      public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("id");
      }
    });
    if(tagId.isEmpty()){
      return null;
    }else{
      return tagId.get(0);
    }
  }

}
