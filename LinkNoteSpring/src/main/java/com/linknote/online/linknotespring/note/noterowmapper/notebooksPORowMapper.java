package com.linknote.online.linknotespring.note.noterowmapper;

import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class notebooksPORowMapper implements RowMapper<NotebooksPO> {

  @Override
  public NotebooksPO mapRow(ResultSet rs, int rowNum) throws SQLException {
    NotebooksPO notebooksPO = new NotebooksPO();
    notebooksPO.setName(rs.getString("name"));
    notebooksPO.setNotebookId(rs.getInt("notebookId"));
    notebooksPO.setDescription(rs.getString("description"));
    notebooksPO.setSelected(rs.getBoolean("selected"));
    return notebooksPO;
  }
}
