package com.joeyliao.linknoteresource.collaborator.rowmapper;

import com.joeyliao.linknoteresource.collaborator.po.NotebookOwnerDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class NotebookOwnerRowMapper implements RowMapper<NotebookOwnerDTO> {

  @Override
  public NotebookOwnerDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
    NotebookOwnerDTO dto = new NotebookOwnerDTO();
    dto.setOwnerEmail(rs.getString("email"));
    dto.setOwnerName(rs.getString("username"));
    return dto;
  }
}
