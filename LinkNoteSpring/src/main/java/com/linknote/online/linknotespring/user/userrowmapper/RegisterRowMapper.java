package com.linknote.online.linknotespring.user.userrowmapper;

import com.linknote.online.linknotespring.user.userpo.UserEmailPo;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class RegisterRowMapper implements RowMapper<UserEmailPo> {


  @Override
  public UserEmailPo mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserEmailPo userEmailPo = new UserEmailPo();
    userEmailPo.setEmail(rs.getString("email"));
    return userEmailPo;
  }
}
