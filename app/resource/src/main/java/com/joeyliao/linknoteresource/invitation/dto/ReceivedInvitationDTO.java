package com.joeyliao.linknoteresource.invitation.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class ReceivedInvitationDTO extends InvitationDTO {
  private String inviterName;
  private String inviterEmail;
}
