package com.joeyliao.linknoteresource.invitation.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class InvitationDTO {
  private String notebookName;
  private String notebookId;
  private String message;
  private Timestamp createDate;
}
