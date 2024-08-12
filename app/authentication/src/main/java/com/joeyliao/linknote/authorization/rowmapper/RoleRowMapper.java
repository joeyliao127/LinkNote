package com.joeyliao.linknote.authorization.rowmapper;

import com.joeyliao.linknote.authorization.enums.Role;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class RoleRowMapper implements RowMapper<String> {

  @Override
  public String mapRow(ResultSet rs, int rowNum) throws SQLException {
    return rs.getObject("name", String.class);
  }
}
