package com.joeyliao.linknote.user.dao;

import com.joeyliao.linknote.user.dto.RegisterRequestDto;
import com.joeyliao.linknote.user.dto.SignInRequestDto;
import com.joeyliao.linknote.user.po.UserEmailPo;
import com.joeyliao.linknote.user.po.UserInfoPO;
import com.joeyliao.linknote.user.rowmapper.RegisterRowMapper;
import com.joeyliao.linknote.user.rowmapper.SignInRowMapper;
import com.joeyliao.linknote.user.rowmapper.UeserIdRowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserDAOImpl implements UserDAO{
  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public List<UserEmailPo> getByEmail(String email) {
    System.out.println("進入驗證DAO");
    String sql = "SELECT email FROM users WHERE email = :email";
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    return namedParameterJdbcTemplate.query(sql, map, new RegisterRowMapper());
  }


  @Override
  public String getUserIdByEmail(String email) {
    String sql = "SELECT id FROM users WHERE email = :email";
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    List<String> result = namedParameterJdbcTemplate.query(sql, map, new UeserIdRowMapper());

    if(result.isEmpty()) {
      return null;
    }else{
      return result.get(0);
    }
  }


  @Override
  public Boolean createUser(RegisterRequestDto registerRequestDto) {
    String sql = "INSERT INTO "
               + "users(id, username, password, email) "
               + "VALUES(:id, :username, :password, :email)";

    log.info("userDAO: 新增使用者email:" + registerRequestDto.getEmail());
    Map<String, Object> map = new HashMap<>();
    map.put("id", registerRequestDto.getUuid().toString());
    map.put("email", registerRequestDto.getEmail());
    map.put("username", registerRequestDto.getUsername());
    map.put("password", registerRequestDto.getPassword());
    int impactRow = namedParameterJdbcTemplate.update(sql, map);
    return impactRow == 1;
  }

  @Override
  public String checkUserIdNotExist(UUID uuid) {
    String sql = """
        SELECT id FROM users WHERE id = :id
        """;

    Map<String ,String> map = new HashMap<>();
    map.put("id", uuid.toString());

    List<String> list = namedParameterJdbcTemplate.query(sql, map, new RowMapper<String>() {
      @Override
      public String mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getString("id");
      }
    });

    if(list.isEmpty()){
      return null;
    }else{
      return list.get(0);
    }
  }

  @Override
  public void updateUser(RegisterRequestDto registerRequestDto) {
    String sql = """
        UPDATE users SET username=:username, password=:password
        WHERE email=:email
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("email", registerRequestDto.getEmail());
    map.put("username", registerRequestDto.getUsername());
    map.put("password", registerRequestDto.getPassword());
    namedParameterJdbcTemplate.update(sql, map);
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
  public List<UserInfoPO> getByTokenUserIdAndEmailForToken(String email, String userId) {
    String sql = "SELECT id as userId, username, email, status FROM users WHERE email = :email and id = :userId and status = 1";
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    map.put("userId", userId);
    return namedParameterJdbcTemplate.query(sql, map, new SignInRowMapper());
  }
}
