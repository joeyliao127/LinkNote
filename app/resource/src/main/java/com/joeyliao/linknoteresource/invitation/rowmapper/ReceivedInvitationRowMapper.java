package com.joeyliao.linknoteresource.invitation.rowmapper;

import com.joeyliao.linknoteresource.invitation.dto.ReceivedInvitationDTO;
import com.joeyliao.linknoteresource.invitation.dto.SentInvitationDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

public class ReceivedInvitationRowMapper implements RowMapper<ReceivedInvitationDTO> {

  @Override
  public ReceivedInvitationDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
    ReceivedInvitationDTO dto = new ReceivedInvitationDTO();
    dto.setInviterName(rs.getString("inviterName"));
    dto.setInviterEmail(rs.getString("inviterEmail"));
    dto.setNotebookId(rs.getString("notebookId"));
    dto.setNotebookName(rs.getString("notebookName"));
    dto.setMessage(rs.getString("message"));
    dto.setCreateDate(rs.getTimestamp("createDate"));
    return dto;
  }
}
