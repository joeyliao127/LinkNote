package com.linknote.online.linknotespring.user.userrowmapper;

import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class SignInRowMapper implements RowMapper<UserInfoPO> {

  @Override
  public UserInfoPO mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserInfoPO userInfoPO = new UserInfoPO();
    userInfoPO.setEmail(rs.getString("email"));
    userInfoPO.setUserId(rs.getInt("userId"));
    userInfoPO.setUsername(rs.getString("username"));
    return userInfoPO;
  }
}
