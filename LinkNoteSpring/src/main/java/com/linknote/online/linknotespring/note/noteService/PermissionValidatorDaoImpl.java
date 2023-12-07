package com.linknote.online.linknotespring.note.noteService;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class PermissionValidatorDaoImpl implements PermissionValidatorDao{

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public Integer verifyNotebookByUserIdAndNotebookId(Integer notebookId, Integer userId) {
    String sql = "SELECT id FROM notebooks WHERE userId = :userId AND id = :notebookId";
    Map<String, Object> map = new HashMap<>();
    map.put("userId", userId);
    map.put("notebookId", notebookId);
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
