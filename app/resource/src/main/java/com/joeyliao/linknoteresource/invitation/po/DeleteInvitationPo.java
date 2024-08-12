package com.joeyliao.linknoteresource.invitation.po;

import lombok.Data;

@Data
public class DeleteInvitationPo {
  private String notebookId;
  private String invitationId;
  private String userEmail;
  private String Authorization;
}
