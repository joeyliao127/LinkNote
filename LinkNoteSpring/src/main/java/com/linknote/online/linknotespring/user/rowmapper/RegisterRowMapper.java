package com.linknote.online.linknotespring.user.rowmapper;

import com.linknote.online.linknotespring.user.model.UserEmailModel;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class RegisterRowMapper implements RowMapper<UserEmailModel> {


  @Override
  public UserEmailModel mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserEmailModel userEmailModel = new UserEmailModel();
    userEmailModel.setEmail(rs.getString("email"));
    return userEmailModel;
  }
}
