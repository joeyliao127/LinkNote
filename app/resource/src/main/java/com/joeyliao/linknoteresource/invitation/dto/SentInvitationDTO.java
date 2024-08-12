package com.joeyliao.linknoteresource.invitation.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class SentInvitationDTO extends InvitationDTO{
  private String inviteeName;
  private String inviteeEmail;
}
