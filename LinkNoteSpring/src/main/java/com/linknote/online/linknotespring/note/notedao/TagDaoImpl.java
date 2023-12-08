package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.GetTagsParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notepo.po.TagPO;
import com.linknote.online.linknotespring.note.noterowmapper.TagsRowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
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
  public List<TagPO> getNotebookTags(GetTagsParamDto params) {
    String sql;
    if(params.getCollaborators()){
      sql = "SELECT t.name as name, t.id as id FROM tags t "
          + "JOIN notebooks n ON t.notebookId = n.id "
          + "JOIN collaborators c ON c.notebookId = n.id "
          + "WHERE c.userId =:userId AND t.notebookId = :notebookId";
    }else{
      sql = "SELECT t.name as name, t.id as id FROM tags t "
          + "JOIN notebooks n ON t.notebookId = n.id "
          + "JOIN users u ON n.userId = u.id "
          + "WHERE n.userId = :userId AND t.notebookId = :notebookId";
    }

    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", params.getNotebookId());
    map.put("userId", params.getUserId());
    return namedParameterJdbcTemplate.query(sql, map, new TagsRowMapper());
  }

  @Override
  public List<TagPO> getNoteTags(Integer noteId) {
    String sql = "SELECT t.id as id, t.name as name FROM tags t "
        + "JOIN notes_tags nt ON t.id = nt.tagId "
        + "JOIN notes n ON nt.noteId = n.id "
        + "WHERE n.id = :noteId";
    Map<String, Object> map = new HashMap<>();
    map.put("noteId", noteId);
    return namedParameterJdbcTemplate.query(sql, map, new TagsRowMapper());
  }

  @Override
  public Integer createNotebookTag(String tag, Integer notebookId) {
    String sql = "INSERT INTO tags (name, notebookId) VALUE(:tag, :notebookId)";
    Map<String, Object> map = new HashMap<>();
    KeyHolder keyHolder = new GeneratedKeyHolder();
    map.put("tag", tag);
    map.put("notebookId", notebookId);
    namedParameterJdbcTemplate.update(sql,new MapSqlParameterSource(map), keyHolder);
    return Objects.requireNonNull(keyHolder.getKey()).intValue();
  }

  @Override
  public void createNotebookTags(List<String> tagList, Integer notebookId) {
    String sql = "INSERT INTO tags (name, notebookId) VALUE(:tag, :notebookId)";
    MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[tagList.size()];
    for(int i=0; i< tagList.size(); i++){
      parameterSources[i] = new MapSqlParameterSource();
      parameterSources[i].addValue("tag", tagList.get(i));
      parameterSources[i].addValue("notebookId", notebookId);
    };
    namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
  }

  @Override
  public void createNoteTags(UpdateNoteTagParamDto param) {
    String sql = "INSERT INTO notes_tags (noteId, tagId) VALUES (:noteId, :tagId)";
    MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[param.getTags().size()];
    for(int i=0; i < param.getTags().size(); i++){
      MapSqlParameterSource parameterSource = new MapSqlParameterSource();
      parameterSources[i] = parameterSource.addValue("noteId", param.getNoteId());
      parameterSources[i] = parameterSource.addValue("tagId", param.getTags().get(i).getTagId());
    }
    namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
  }

  @Override
  public void deleteNoteTags(Integer noteId) {
    String sql = "DELETE FROM notes_tags WHERE noteId = :noteId";
    Map<String, Object> map = new HashMap<>();
    map.put("noteId", noteId);
    namedParameterJdbcTemplate.update(sql, map);
  }

}
