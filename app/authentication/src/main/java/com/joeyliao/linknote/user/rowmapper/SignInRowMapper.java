package com.joeyliao.linknote.user.rowmapper;

import com.joeyliao.linknote.user.po.UserInfoPO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class SignInRowMapper implements RowMapper<UserInfoPO> {

  @Override
  public UserInfoPO mapRow(ResultSet rs, int rowNum) throws SQLException {
    UserInfoPO userInfoPO = new UserInfoPO();
    userInfoPO.setEmail(rs.getString("email"));
    userInfoPO.setUserId(rs.getString("userId"));
    userInfoPO.setUsername(rs.getString("username"));
    userInfoPO.setStatus(rs.getBoolean("status"));
    return userInfoPO;
  }
}
