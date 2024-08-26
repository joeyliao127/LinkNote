package com.joeyliao.linknoteresource.po.invitation;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateInvitationPo {
  @NotBlank(message = "email not blank")
  @Email(message = "invalid email format")
  private String inviteeEmail;
  private String notebookId;
  private String inviterEmail;
  private String authorization;
  private String message;
}
