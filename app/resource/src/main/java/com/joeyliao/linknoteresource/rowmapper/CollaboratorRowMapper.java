package com.joeyliao.linknoteresource.rowmapper;

import com.joeyliao.linknoteresource.dto.collaboratordto.CollaboratorsDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class CollaboratorRowMapper implements RowMapper<CollaboratorsDTO> {

  @Override
  public CollaboratorsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
    CollaboratorsDTO dto = new CollaboratorsDTO();
    dto.setName(rs.getString("name"));
    dto.setUserEmail(rs.getString("email"));
    return dto;
  }
}
