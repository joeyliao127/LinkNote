package com.linknote.online.linknotespring.user.userrowmapper;

import com.linknote.online.linknotespring.user.userdto.AuthenticationRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class AuthenticationRowMapper implements RowMapper<AuthenticationRequest> {

  @Override
  public AuthenticationRequest mapRow(ResultSet rs, int rowNum) throws SQLException {
    AuthenticationRequest authenticationRequest = new AuthenticationRequest();
    authenticationRequest.setEmail(rs.getString("email"));
    authenticationRequest.setPassword(rs.getString("password"));
    return authenticationRequest;
  }
}
