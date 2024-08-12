package com.joeyliao.linknoteresource.generic.uuidgenerator.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class UUIDRowMapper implements RowMapper<String> {

  @Override
  public String mapRow(ResultSet rs, int rowNum) throws SQLException {
    return rs.getString("id");
  }
}
