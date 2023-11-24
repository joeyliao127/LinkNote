package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.noterowmapper.notebooksPORowMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class NoteDaoImpl implements NoteDao{
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Override
  public List<NotebooksPO> getNotebooks(int offset, int userId) {
    String sql = "SELECT id as notebookId, name, selected, description FROM notebooks n"
        + "JOIN users u ON u.id = n.userId"
        + "WHERE userId = :userId"
        + "LIMIT 20 OFFSET :offset";

    Map<String, Object> map = new HashMap<>();
    map.put("userId", userId);
    map.put("offset", offset);
    return namedParameterJdbcTemplate.query(sql, map, new notebooksPORowMapper());
  }
}
