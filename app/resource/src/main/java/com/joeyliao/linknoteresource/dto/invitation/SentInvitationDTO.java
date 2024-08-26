package com.joeyliao.linknoteresource.dto.invitation;

import lombok.Data;

@Data
public class SentInvitationDTO extends InvitationDTO{
  private String inviteeName;
  private String inviteeEmail;
}
