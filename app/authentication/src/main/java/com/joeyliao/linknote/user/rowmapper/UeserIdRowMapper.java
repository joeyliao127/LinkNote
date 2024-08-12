package com.joeyliao.linknote.user.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class UeserIdRowMapper implements RowMapper<String> {


  @Override
  public String mapRow(ResultSet rs, int rowNum) throws SQLException {
    return rs.getString("id");
  }
}
