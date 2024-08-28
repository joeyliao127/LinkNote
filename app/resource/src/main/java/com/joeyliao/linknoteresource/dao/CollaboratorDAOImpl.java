package com.joeyliao.linknoteresource.dao;

import com.joeyliao.linknoteresource.dto.collaboratordto.CollaboratorsDTO;
import com.joeyliao.linknoteresource.po.collaboratorpo.DeleteCollaboratorPo;
import com.joeyliao.linknoteresource.po.collaboratorpo.GetCollaboratorsRequestPo;
import com.joeyliao.linknoteresource.rowmapper.CollaboratorRowMapper;
import com.joeyliao.linknoteresource.enums.Role;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CollaboratorDAOImpl implements CollaboratorDAO {

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;
  @Override
  public List<CollaboratorsDTO> getCollaborators(GetCollaboratorsRequestPo po) {
    String sql = """
        SELECT u.username as name, u.email as email 
        FROM notebooks_users_role nur
        JOIN users u ON nur.userId = u.id
        JOIN notebooks n ON nur.notebookId = n.id
        WHERE n.id = :notebookId AND nur.roleId = :roleId
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("notebookId", po.getNotebookId());
    map.put("roleId", roleMapper(Role.COLLABORATOR));
    return namedParameterJdbcTemplate.query(sql, map, new CollaboratorRowMapper());
  }

  @Override
  public void deleteCollaborator(DeleteCollaboratorPo po) {
    String sql = """
        DELETE n FROM notebooks_users_role n
            JOIN users u ON n.userId = u.id
        WHERE u.email = :email AND n.notebookId = :notebookId
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("email", po.getUserEmail());
    map.put("notebookId", po.getNotebookId());
    namedParameterJdbcTemplate.update(sql, map);
  }


  @Override
  public void createCollaborator(String inviteeEmail, String notebookId) {
    String sql = """
        INSERT INTO notebooks_users_role(userId, notebookId, roleId)
        VALUES (:inviteeEmail, :notebookId, :roleId)
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("inviteeEmail", inviteeEmail);
    map.put("notebookId", notebookId);
    map.put("roleId", roleMapper(Role.COLLABORATOR));
    namedParameterJdbcTemplate.update(sql, map);
  }


  private Integer roleMapper(Role role){
    Map<Role, Integer> map = new HashMap<>();
    map.put(Role.OWNER, 1);
    map.put(Role.COLLABORATOR, 2);
    map.put(Role.MEMBER, 3);
    map.put(Role.GUEST, 4);
    return map.get(role);
  }
}
