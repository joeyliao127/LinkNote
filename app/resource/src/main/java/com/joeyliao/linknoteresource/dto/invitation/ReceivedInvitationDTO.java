package com.joeyliao.linknoteresource.dto.invitation;

import lombok.Data;

@Data
public class ReceivedInvitationDTO extends InvitationDTO {
  private String inviterName;
  private String inviterEmail;
}
