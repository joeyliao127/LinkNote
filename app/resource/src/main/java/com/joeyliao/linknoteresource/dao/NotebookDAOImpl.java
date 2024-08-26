package com.joeyliao.linknoteresource.dao;

import com.joeyliao.linknoteresource.po.collaboratorpo.NotebookOwnerDTO;
import com.joeyliao.linknoteresource.rowmapper.NotebookOwnerRowMapper;
import com.joeyliao.linknoteresource.enums.generic.Role;
import com.joeyliao.linknoteresource.dto.notebookdto.NotebooksDTO;
import com.joeyliao.linknoteresource.po.notebookpo.GetNotebooksRequestPo;
import com.joeyliao.linknoteresource.po.notebookpo.CreateNotebookRequestPo;
import com.joeyliao.linknoteresource.po.notebookpo.UpdateNotebookPo;
import com.joeyliao.linknoteresource.rowmapper.AllNotebooksRowMapper;
import com.joeyliao.linknoteresource.rowmapper.NotebookNameRowMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class NotebookDAOImpl implements NotebookDAO {

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public void createNotebook(CreateNotebookRequestPo po, String id) {
    String sql = """
        INSERT INTO notebooks(id, name, description, userId)
        VALUES (:id ,:name, :description, :userId);
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("id", id);
    map.put("name", po.getName());
    map.put("description", po.getDescription());
    map.put("userId", po.getUserId());
    namedParameterJdbcTemplate.update(sql, map);
    setNotebookUserRoleAsOwner(id, po.getUserId());
  }

  @Override
  public List<NotebooksDTO> getNotebooks(GetNotebooksRequestPo po) {
    return getNotebooksCollation(po, false);
  }

  @Override
  public List<NotebooksDTO> getCoNotebooks(GetNotebooksRequestPo po) {
    return getNotebooksCollation(po, true);
  }

  @Override
  public Integer updateNotebook(UpdateNotebookPo po) {
    String sql = """
        UPDATE notebooks SET name = :name,description = :description
        WHERE id = :notebookId;
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", po.getNotebookId());
    map.put("name", po.getName());
    map.put("description", po.getDescription());
    return namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public Integer deleteNotebook(String notebookId) {
    String sql = """
        DELETE FROM notebooks WHERE id = :id;
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("id", notebookId);
    return namedParameterJdbcTemplate.update(sql, map);
  }

  private List<NotebooksDTO> getNotebooksCollation(GetNotebooksRequestPo po, Boolean isCoNotebook) {
    String sql = null;
    if (isCoNotebook) {
      sql = """
          SELECT n.id, n.name, n.description FROM notebooks n 
          JOIN notebooks_users_role nur ON n.id = nur.notebookId 
          WHERE nur.userId = :userId AND nur.roleId = 2 
          """;

    } else {
      sql = """
          SELECT n.id, n.name, n.description FROM notebooks n 
          JOIN users u ON n.userId = u.id  
          WHERE n.userId = :userId 
          """;
    }
    if (!Objects.equals(po.getKeyword(), "null")) {
      sql += "AND n.name like :keyword ORDER BY n.create_date DESC ";
    }
    sql += "LIMIT :limit OFFSET :offset ";
    Map<String, Object> map = new HashMap<>();
    map.put("userId", po.getUserId());
    map.put("keyword", "%" + po.getKeyword() + "%");
    map.put("offset", po.getOffset());
    map.put("limit", po.getLimit() + 1);
    log.info("userId: " + po.getUserId());
    List<NotebooksDTO> list = namedParameterJdbcTemplate.query(sql, map, new AllNotebooksRowMapper());
    return namedParameterJdbcTemplate.query(sql, map, new AllNotebooksRowMapper());
  }

  private void setNotebookUserRoleAsOwner(String notebookId, String userId){
    String sql = """
        INSERT INTO notebooks_users_role(notebookId, userId, roleId)
        VALUES (:notebookId, :userId, :role);
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    map.put("userId", userId);
    map.put("role", roleMap(Role.OWNER));
    namedParameterJdbcTemplate.update(sql, map);
  }

  private Integer roleMap(Role role){
    Map<Role, Integer> map = new HashMap<>();
    map.put(Role.OWNER, 1);
    map.put(Role.COLLABORATOR, 2);
    map.put(Role.MEMBER, 3);
    map.put(Role.GUEST, 4);
    return map.get(role);
  }

  @Override
  public NotebookOwnerDTO getNotebookOwner(String notebookId) {
    String sql = """
        SELECT u.username as username, u.email as email 
        FROM notebooks n
        JOIN users u ON n.userId = u.id
        WHERE n.id = :notebookId
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", notebookId);
    List<NotebookOwnerDTO> dtos = namedParameterJdbcTemplate.query(sql, map, new NotebookOwnerRowMapper());
    return dtos.get(0);
  }

  @Override
  public NotebooksDTO getNotebook(String notebookId) {
    String sql = """
        SELECT id, name, description FROM notebooks WHERE id = :id
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("id", notebookId);
    List<NotebooksDTO> list =  namedParameterJdbcTemplate.query(sql, map, new NotebookNameRowMapper());
    if(list.isEmpty()){
      return null;
    }
    return list.get(0);
  }
}
