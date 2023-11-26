package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboraotrsParamDto;
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

  @Override
  public Integer getCollaboratorsCount(Integer ownerId, Integer notebookId) {
    String sql = "SELECT COUNT(*) as count FROM notebookCollaborators"
        + " WHERE owner = :ownerId AND notebookId = :notebookId";
    Map<String, Object> map = new HashMap<>();
    map.put("ownerId", ownerId);
    map.put("notebookId", notebookId);

    return namedParameterJdbcTemplate.query(sql, map, new RowMapper<Integer>() {
      @Override
      public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("count");
      }
    }).get(0);
  }

  @Override
  public void createNotTags(Integer tagId, Integer noteId) {
    String sql = "INSERT INTO notes_tags (noteId, tagId) VALUES(:noteId, :tagId)";
    Map<String, Object> map = new HashMap<>();
    map.put("noteId", noteId);
    map.put("tagId", tagId);
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void createNotebookCollaborators(List<Integer> collaboratorList, Integer notebookId, Integer ownerId) {
    String sql = "INSERT INTO notebookCollaborators (userId, notebookId, owner) VALUES (:userId, :notebookId, :ownerId)";
    MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[collaboratorList.size()];
    for(int i=0; i<collaboratorList.size(); i++){
      parameterSources[i] = new MapSqlParameterSource();
      parameterSources[i].addValue("userId", collaboratorList.get(i));
      parameterSources[i].addValue("notebookId", notebookId);
      parameterSources[i].addValue("ownerId", ownerId);
    }
    namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
  }

  @Override
  public void createCollaborator(CreateCollaboratorParamsDto params) {
    String sql = "INSERT INTO notebookCollaborators (owner, notebookId, userId) VALUES (:ownerId, :notebookId, :userId)";
    Map<String, Object> map = new HashMap<>();
    map.put("ownerId", params.getUserId());
    map.put("notebookId", params.getNotebookId());
    map.put("userId", params.getCollaboratorId());
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void updateNotebookTags(Integer notebookId, Integer tagId) {
    String sql = "INSERT INTO notebooks_tags (notebookId, tagId)"
        + " VALUES (:notebookId, :tagId)";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    map.put("tagId", tagId);
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void deleteCollaborators(DeleteCollaboraotrsParamDto params) {
    String sql = "DELETE FROM ntoebookCollaborators WHERE userId = :userId AND notebookId = :notebookId";
    Map<String, Object> map = new HashMap<>();
    map.put("userId", params.getUserId());
    map.put("notebookId", params.getNotebookId());
    namedParameterJdbcTemplate.update(sql, map);
  }
}
