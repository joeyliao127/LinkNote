package com.linknote.online.linknotespring.user.userdao;

import com.linknote.online.linknotespring.user.userdto.SignInRequestDto;
import com.linknote.online.linknotespring.user.userdto.RegisterRequestDto;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import com.linknote.online.linknotespring.user.userrowmapper.SignInRowMapper;
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
    System.out.println("進入驗證DAO");
    String sql = "SELECT email FROM members WHERE email = :email";
    Map<String, Object> map = new HashMap<>();
    System.out.println("製作hashmap");
    map.put("email", email);
    System.out.print("製作完成");
    return namedParameterJdbcTemplate.query(sql, map, new RegisterRowMapper());
  }

  @Override
  public Integer createUser(RegisterRequestDto registerRequestDto) {
    String sql = "INSERT INTO "
               + "members(username, password, email) "
               + "VALUES(:username, :password, :email)";
    Map<String, Object> map = new HashMap<>();
    map.put("email", registerRequestDto.getEmail());
    map.put("username", registerRequestDto.getUsername());
    map.put("password", registerRequestDto.getPassword());
    KeyHolder keyHolder = new GeneratedKeyHolder();
    int impactRow = namedParameterJdbcTemplate.update(sql, new MapSqlParameterSource(map), keyHolder);
    if(impactRow == 1){
      return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }else{
      return null;
    }

  }

  @Override
  public List<UserInfoPO> getByEmailAndPassword(
      SignInRequestDto signInRequestDto) {
    String sql = "SELECT id as userId, username, email, status FROM members WHERE email = :email and password = :password";
    Map<String, String> map = new HashMap<>();
    map.put("email", signInRequestDto.getEmail());
    map.put("password", signInRequestDto.getPassword());
    return namedParameterJdbcTemplate.query(sql, map, new SignInRowMapper());
  }

  @Override
  public List<UserInfoPO> getByTokenUserIdAndEmailForToken(String email, Integer userId) {
    String sql = "SELECT id as userId, username, email, status FROM members WHERE email = :email and id = :userId";
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    map.put("userId", userId);
    return namedParameterJdbcTemplate.query(sql, map, new SignInRowMapper());
  }
}
