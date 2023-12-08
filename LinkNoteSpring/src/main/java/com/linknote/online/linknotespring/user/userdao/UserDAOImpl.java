package com.linknote.online.linknotespring.user.userdao;

import com.linknote.online.linknotespring.user.userdto.SignInRequestDto;
import com.linknote.online.linknotespring.user.userdto.RegisterRequestDto;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import com.linknote.online.linknotespring.user.userrowmapper.UeserIdRowMapper;
import com.linknote.online.linknotespring.user.userrowmapper.SignInRowMapper;
import com.linknote.online.linknotespring.user.userrowmapper.RegisterRowMapper;
import com.linknote.online.linknotespring.user.userpo.UserEmailPo;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
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
    String sql = "SELECT email FROM users WHERE email = :email";
    Map<String, Object> map = new HashMap<>();
    System.out.println("製作hashmap");
    map.put("email", email);
    System.out.print("製作完成");
    return namedParameterJdbcTemplate.query(sql, map, new RegisterRowMapper());
  }

  @Override
  public UserInfoPO getUserInfo(String email) {
    String sql = "SELECT id, username, email, status FROM users WHERE email = :email";
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
   List<UserInfoPO> result = namedParameterJdbcTemplate.query(sql, map, new RowMapper<UserInfoPO>() {
     @Override
     public UserInfoPO mapRow(ResultSet rs, int rowNum) throws SQLException {
       UserInfoPO userInfoPO = new UserInfoPO();
       userInfoPO.setUserId(rs.getInt("id"));
       userInfoPO.setUsername(rs.getString("username"));
       userInfoPO.setStatus(rs.getBoolean("status"));
       userInfoPO.setEmail(rs.getString("email"));
       return userInfoPO;
     }
   });
   if(result.isEmpty()){
     return null;
   }else {
     return result.get(0);
   }
  }

  @Override
  public Integer getUserIdByEmail(String email) {
    String sql = "SELECT id FROM users WHERE email = :email";
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    List<Integer> result = namedParameterJdbcTemplate.query(sql, map, new UeserIdRowMapper());
    if(result.isEmpty()) {
      return null;
    }else{
      return result.get(0);
    }
  }

  @Override
  public String getUsernameByUserId(Integer userId) {
    String sql = "SELECT username FROM users WHERE id = :userId";
    Map<String , Object> map = new HashMap<>();
    map.put("userId", userId);
    List<String> username = namedParameterJdbcTemplate.query(sql, map, new RowMapper<String>() {
      @Override
      public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("username");
      }
    });
    if(username.isEmpty()){
      return null;
    }else {
      return username.get(0);
    }

  }

  @Override
  public Integer verifyUserIdAndEmail(String email, Integer userId) {
    String sql = "SELECT id FROM users WHERE email = :email AND id = :userId";
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    map.put("userId", userId);
    List<Integer> result = namedParameterJdbcTemplate.query(sql, map, new UeserIdRowMapper());
    if(result.isEmpty()){
      return null;
    }else {
      return result.get(0);
    }
  }

  @Override
  public Integer createUser(RegisterRequestDto registerRequestDto) {
    String sql = "INSERT INTO "
               + "users(username, password, email) "
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
  public String getUsernameByNotebookId(Integer notebookId) {
    String sql = "SELECT u.username FROM users u "
        + "JOIN notebooks n ON n.userId = u.id "
        + "WHERE n.id = :notebookId";
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    List<String> username = namedParameterJdbcTemplate.query(sql, map, new RowMapper<String>() {
      @Override
      public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("username");
      }
    });
    if(username.isEmpty()){
      return null;
    }else{
      return username.get(0);
    }
  }

  @Override
  public List<UserInfoPO> getByEmailAndPassword(
      SignInRequestDto signInRequestDto) {
    String sql = "SELECT id as userId, username, email, status FROM users WHERE email = :email and password = :password";
    Map<String, String> map = new HashMap<>();
    map.put("email", signInRequestDto.getEmail());
    map.put("password", signInRequestDto.getPassword());
    return namedParameterJdbcTemplate.query(sql, map, new SignInRowMapper());
  }

  @Override
  public List<UserInfoPO> getByTokenUserIdAndEmailForToken(String email, Integer userId) {
    String sql = "SELECT id as userId, username, email, status FROM users WHERE email = :email and id = :userId and status = 1";
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    map.put("userId", userId);
    return namedParameterJdbcTemplate.query(sql, map, new SignInRowMapper());
  }
}
