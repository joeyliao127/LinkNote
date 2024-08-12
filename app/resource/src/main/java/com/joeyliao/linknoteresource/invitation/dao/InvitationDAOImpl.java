package com.joeyliao.linknoteresource.invitation.dao;

import com.joeyliao.linknoteresource.invitation.dto.InvitationDTO;
import com.joeyliao.linknoteresource.invitation.dto.ReceivedInvitationDTO;
import com.joeyliao.linknoteresource.invitation.dto.SentInvitationDTO;
import com.joeyliao.linknoteresource.invitation.po.CreateInvitationPo;
import com.joeyliao.linknoteresource.invitation.po.DeleteInvitationPo;
import com.joeyliao.linknoteresource.invitation.po.GetInvitationRequestPo;
import com.joeyliao.linknoteresource.invitation.po.UpdateInvitationPo;
import com.joeyliao.linknoteresource.invitation.rowmapper.CheckInvitationRowMapper;
import com.joeyliao.linknoteresource.invitation.rowmapper.ReceivedInvitationRowMapper;
import com.joeyliao.linknoteresource.invitation.rowmapper.SentInvitationRowMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class InvitationDAOImpl implements InvitationDAO {

  @Autowired
  NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public void createInvitation(CreateInvitationPo po) {
    String sql = """
        INSERT INTO invitations(inviterEmail, inviteeEmail, notebookId, message)
        VALUES (:inviterEmail, :inviteeEmail, :notebookId, :message)
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("inviterEmail", po.getInviterEmail());
    map.put("inviteeEmail", po.getInviteeEmail());
    map.put("notebookId", po.getNotebookId());
    map.put("message", po.getMessage());
    namedParameterJdbcTemplate.update(sql, map);
  }

  @Override
  public List<Integer> checkInvitationNotExist(CreateInvitationPo po) {
    String sql = """
        SELECT id FROM invitations
        WHERE inviteeEmail = :inviteeEmail
        AND notebookId = :notebookId
        AND isPending = 1
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("inviteeEmail", po.getInviteeEmail());
    map.put("notebookId", po.getNotebookId());

    return namedParameterJdbcTemplate.query(sql ,map, new CheckInvitationRowMapper());

  }

  @Override
  public List<SentInvitationDTO> getSentInvitation(GetInvitationRequestPo po) {
    String sql = """
        SELECT u.username as inviteeName, u.email as inviteeEmail
        , n.id as notebookId, n.name as notebookName
        , i.message, i.date as createDate
        FROM invitations i JOIN users u ON i.inviteeEmail = u.email
        JOIN notebooks n ON i.notebookId = n.id
        WHERE i.inviterEmail = :inviterEmail AND i.isPending = 1 limit :limit offset :offset
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("inviterEmail", po.getUserEmail());
    map.put("limit", po.getLimit());
    map.put("offset", po.getOffset());
    return namedParameterJdbcTemplate.query(sql, map, new SentInvitationRowMapper());
  }

  @Override
  public List<ReceivedInvitationDTO> getReceivedInvitation(GetInvitationRequestPo po) {
    String sql = """
        SELECT u.username as inviterName, u.email as inviterEmail
             , n.id as notebookId, n.name as notebookName
             , i.message, i.date as createDate
        FROM invitations i JOIN users u ON i.inviterEmail = u.email
                           JOIN notebooks n ON i.notebookId = n.id
        WHERE i.inviteeEmail = :inviteeEmail AND i.isPending = 1 limit :limit offset :offset
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("inviteeEmail", po.getUserEmail());
    map.put("limit", po.getLimit());
    map.put("offset", po.getOffset());
    return namedParameterJdbcTemplate.query(sql, map, new ReceivedInvitationRowMapper());
  }

  @Override
  public void deleteInvitation(DeleteInvitationPo po) {
    String sql = """
        DELETE FROM invitations 
        WHERE inviterEmail = :inviterEmail AND notebookId = :notebookId
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("inviterEmail", po.getUserEmail());
    map.put("notebookId", po.getNotebookId());
    namedParameterJdbcTemplate.update(sql ,map);
  }

  @Override
  public void updateInvitation(UpdateInvitationPo po) {
    String sql = """
        UPDATE invitations SET isPending = 0, isAccept = :isAccept
        WHERE notebookId = :notebookId AND inviteeEmail = :inviteeEmail
        """;
    Map<String, Object> map = new HashMap<>();
    map.put("inviteeEmail", po.getInviteeEmail());
    map.put("notebookId", po.getNotebookId());
    map.put("isAccept", po.getIsAccept());
    namedParameterJdbcTemplate.update(sql,map);
  }
}
