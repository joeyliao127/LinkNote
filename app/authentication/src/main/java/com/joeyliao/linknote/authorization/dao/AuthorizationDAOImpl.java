package com.joeyliao.linknote.authorization.dao;

import com.joeyliao.linknote.authorization.enums.Action;
import com.joeyliao.linknote.authorization.enums.Role;
import com.joeyliao.linknote.authorization.requestobject.PermissionRequest;
import com.joeyliao.linknote.authorization.rowmapper.PermissionRowMapper;
import com.joeyliao.linknote.authorization.rowmapper.RoleRowMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthorizationDAOImpl implements AuthorizationDAO {

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public Role getRole(String notebookId, String userId) {
    String sql = """
        SELECT r.name FROM notebooks_users_role nur
        JOIN role r ON nur.roleId = r.id
        WHERE nur.userId = :userId AND nur.notebookId = :notebookId;
        """;
    Map<String, String> map = new HashMap<>();
    map.put("userId", userId);
    map.put("notebookId", notebookId);
    List<String> list = namedParameterJdbcTemplate.query(sql, map, new RoleRowMapper());
    if (list.isEmpty()) {
      return null;
    } else {
      return Role.valueOf(list.get(0));
    }
  }

  @Override
  public Action getPermission(PermissionRequest request) {
    log.info("======Auth: 收到Permission驗證請求，以下為申請資訊：======");
    log.info("userId: " + request.getUserId());
    log.info("notebookId:" + request.getNotebookId());
    log.info("操作對象target：" + request.getTarget());
    log.info("Behavior: " + request.getBehavior());
    log.info("Role: " + request.getRole());

    String sql = """
        SELECT a.name as name
        FROM role r
                 JOIN role_permission rp ON r.id = rp.roleId
                 JOIN target t ON rp.target = t.id
                 JOIN behavior b ON rp.behaviorId = b.id
                 JOIN action a ON rp.actionId = a.id
        WHERE  r.name = :role
          AND b.name = :behavior
          AND t.name = :target
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("role", request.getRole().toString());
    map.put("behavior", request.getBehavior().toString());
    map.put("target", request.getTarget().toString());
    List<String> list = namedParameterJdbcTemplate.query(sql, map, new PermissionRowMapper());
    log.info("查詢結果：" + list.get(0));
    return Action.valueOf(list.get(0));
  }
}
