package com.linknote.online.linknotespring.user.userdao;

import com.linknote.online.linknotespring.user.userdto.SignInRequestDto;
import com.linknote.online.linknotespring.user.userdto.RegisterRequestDto;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import com.linknote.online.linknotespring.user.userrowmapper.UeserIdRowMapper;
import com.linknote.online.linknotespring.user.userrowmapper.SignInRowMapper;
import com.linknote.online.linknotespring.user.userrowmapper.RegisterRowMapper;
import com.linknote.online.linknotespring.user.userpo.UserEmailPo;
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
  public List<Integer> getUserIdByEmail(String email) {
    String sql = "SELECT id FROM users WHERE email = :email";
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    return namedParameterJdbcTemplate.query(sql, map, new UeserIdRowMapper());
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

  @Override
  public Integer getCollaboratorsId(Integer userId, Integer notebookId) {
    String sql = "SELECT userId FROM notebookCollaborators"
        + " WHERE notebookId = :notebookId AND userId = :userId";
    Map<String , Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    map.put("userId", userId);
    List<Integer> result = namedParameterJdbcTemplate.query(sql, map, new RowMapper<Integer>() {
      @Override
      public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("userId");
      }
    });
    if(result.isEmpty()){
      return null;
    }else{
      return result.get(0);
    }
  }

  @Override
  public void updatyCollaborator(List<Integer> collaboratorList, Integer notebookId) {
    String sql = "INSERT INTO notebookCollaborators (userId, notebookId) VALUES (:userId, :notebookId)";
    MapSqlParameterSource[] parameterSources = new MapSqlParameterSource[collaboratorList.size()];
    for(int i=0; i<collaboratorList.size(); i++){
      parameterSources[i] = new MapSqlParameterSource();
      parameterSources[i].addValue("userId", collaboratorList.get(i));
      parameterSources[i].addValue("notebookId", notebookId);
    }
    namedParameterJdbcTemplate.batchUpdate(sql, parameterSources);
  }
}
