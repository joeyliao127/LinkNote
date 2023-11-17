package com.linknote.online.linknotespring.user.userdao;

import com.linknote.online.linknotespring.user.userdto.AuthenticationRequest;
import com.linknote.online.linknotespring.user.userdto.RegisterRequest;
import com.linknote.online.linknotespring.user.userrowmapper.RegisterRowMapper;
import com.linknote.online.linknotespring.user.userpo.UserEmailPo;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl implements UserDAO{
  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public List<UserEmailPo> getByEmail(String email) {
    String sql = "SELECT email FROM members WHERE email = :email";
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    return namedParameterJdbcTemplate.query(sql, map, new RegisterRowMapper());
  }

  @Override
  public Integer createUser(RegisterRequest registerRequest) {
    String sql = "INSERT INTO "
               + "members(username, password, email) "
               + "VALUES(:username, :password, :email)";
    Map<String, Object> map = new HashMap<>();
    map.put("email", registerRequest.getEmail());
    map.put("username", registerRequest.getUsername());
    map.put("password", registerRequest.getPassword());
    KeyHolder keyHolder = new GeneratedKeyHolder();
    int impactRow = namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
    if(impactRow == 1){
      return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }else{
      return null;
    }

  }

  @Override
  public List<AuthenticationRequest> getByEmailAndPassword(
      AuthenticationRequest authenticationRequest) {
    String sql = "SELECT email, password FROM members WHERE email = :email and password = :password";
    Map<String, String> map = new HashMap<>();
    map.put("email", authenticationRequest.getEmail());
    map.put("password", authenticationRequest.getPassword());

    return null;
  }
}
