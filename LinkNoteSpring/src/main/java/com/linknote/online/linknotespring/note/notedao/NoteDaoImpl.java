package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.NotebooksQueryParams;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.noterowmapper.notebooksPORowMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class NoteDaoImpl implements NoteDao{
  @Autowired
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Override
  public List<NotebooksPO> getNotebooks(NotebooksQueryParams params, Boolean getCoNotebook) {
    String sql;
    if(getCoNotebook){
      sql = "SELECT n.id as notebookId, n.name as notebookName, n.selected FROM notebooks n "
          + "JOIN notebookCollaborators c ON c.notebookId = n.id "
          + "JOIN users u ON u.id = c.userId "
          + "WHERE u.id = :userId LIMIT :limit OFFSET :offset";

    }else{
      sql = "SELECT n.id as notebookId, n.name as notebookName, n.selected FROM notebooks n "
          + "JOIN users u ON u.id = n.userId "
          + "WHERE userId = :userId LIMIT :limit OFFSET :offset";

    }
    Map<String, Object> map = new HashMap<>();
    map.put("userId", params.getUserId());
    map.put("offset", params.getOffset());
    map.put("limit", params.getLimit() + 1); //+1為了驗證是否有nextPage
    return namedParameterJdbcTemplate.query(sql, map, new notebooksPORowMapper());
  }

}
