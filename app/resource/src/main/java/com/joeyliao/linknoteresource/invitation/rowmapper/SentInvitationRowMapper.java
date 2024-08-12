package com.joeyliao.linknoteresource.invitation.rowmapper;

import com.joeyliao.linknoteresource.invitation.dto.SentInvitationDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class SentInvitationRowMapper implements RowMapper<SentInvitationDTO> {

  @Override
  public SentInvitationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
    SentInvitationDTO dto = new SentInvitationDTO();
    dto.setInviteeName(rs.getString("inviteeName"));
    dto.setInviteeEmail(rs.getString("inviteeEmail"));
    dto.setNotebookId(rs.getString("notebookId"));
    dto.setNotebookName(rs.getString("notebookName"));
    dto.setMessage(rs.getString("message"));
    dto.setCreateDate(rs.getTimestamp("createDate"));
    return dto;
  }
}
