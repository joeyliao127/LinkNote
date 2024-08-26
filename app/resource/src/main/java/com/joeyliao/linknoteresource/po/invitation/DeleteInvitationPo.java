package com.joeyliao.linknoteresource.po.invitation;

import lombok.Data;

@Data
public class DeleteInvitationPo {
  private String notebookId;
  private String invitationId;
  private String userEmail;
  private String Authorization;
}
