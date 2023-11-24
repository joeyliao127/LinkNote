package com.linknote.online.linknotespring.user.userrowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class UeserIdRowMapper implements RowMapper<Integer> {


  @Override
  public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
    return rs.getInt("id");
  }
}
