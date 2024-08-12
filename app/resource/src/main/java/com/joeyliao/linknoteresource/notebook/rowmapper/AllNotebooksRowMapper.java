package com.joeyliao.linknoteresource.notebook.rowmapper;

import com.joeyliao.linknoteresource.notebook.dto.NotebooksDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class AllNotebooksRowMapper implements RowMapper<NotebooksDTO> {

  @Override
  public NotebooksDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
    NotebooksDTO dto = new NotebooksDTO();
    dto.setId(rs.getString("id"));
    dto.setName(rs.getString("name"));
    dto.setDescription(rs.getString("description"));
    return dto;
  }
}
