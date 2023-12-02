package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboratorsParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookParamsDto;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class IntermediaryDaoImpl implements IntermediaryDao {

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public Integer getCollaboratorsCount(Integer ownerId, Integer notebookId) {
    String sql = "SELECT COUNT(*) as count FROM collaborators"
        + " WHERE owner = :ownerId AND notebookId = :notebookId";
    Map<String, Object> map = new HashMap<>();
    System.out.println("ownerId: " + ownerId);
    System.out.println("notebookId: " + notebookId);
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
  public void createNotebookCollaborators(List<Integer> collaboratorList, Integer notebookId, Integer ownerId) {
    String sql = "INSERT INTO collaborators (userId, notebookId, owner) VALUES (:userId, :notebookId, :ownerId)";
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
  public Integer createCollaborator(CreateCollaboratorParamsDto params) {
    String sql = "INSERT INTO collaborators (owner, notebookId, userId) VALUES (:ownerId, :notebookId, :userId)";
    Map<String, Object> map = new HashMap<>();
    map.put("ownerId", params.getUserId());
    map.put("notebookId", params.getNotebookId());
    map.put("userId", params.getCollaboratorId());
    KeyHolder keyHolder = new GeneratedKeyHolder();
    return namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
  }

  @Override
  public void deleteCollaborator(DeleteCollaboratorsParamDto params) {
    String sql = "DELETE FROM collaborators "
        + "WHERE userId = :userId "
        + "AND notebookId = :notebookId "
        + "AND owner = :ownerId";
    Map<String, Object> map = new HashMap<>();
    map.put("userId", params.getCollaboratorId());
    map.put("notebookId", params.getNotebookId());
    map.put("ownerId", params.getUserId());
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public void deleteCollaborators(DeleteNotebookParamsDto params) {
    String sql = "DELETE FROM collaborators WHERE notebookId = :notebookId";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", params.getNotebookId());
    namedParameterJdbcTemplate.update(sql, map);
  }
}
