package com.linknote.online.linknotespring.user.userdao;

import com.linknote.online.linknotespring.user.model.RegisterModel;
import com.linknote.online.linknotespring.user.rowmapper.RegisterRowMapper;
import com.linknote.online.linknotespring.user.model.UserEmailModel;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Repository
public class UserDaoImpl implements UserDAO{
  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public List<UserEmailModel> getByEmail(String email) {
    String sql = "SELECT email FROM members WHERE email = :email";
    Map<String, Object> map = new HashMap<>();
    map.put("email", email);
    List<UserEmailModel> list = namedParameterJdbcTemplate.query(sql, map, new RegisterRowMapper());
    if(!list.isEmpty()){
      System.out.println("email查詢結果：" + list.get(0).getEmail());
    }
    return list;
  }

  @Override
  public Integer createUser(RegisterModel registerModel) {
    String sql = "INSERT INTO "
               + "members(username, password, email) "
               + "VALUES(:username, :password, :email)";
    Map<String, Object> map = new HashMap<>();
    map.put("email", registerModel.getEmail());
    map.put("username", registerModel.getUsername());
    map.put("password", registerModel.getPassword());
    return namedParameterJdbcTemplate.update(sql, map);
  }
}
