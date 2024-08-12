package com.joeyliao.linknoteresource.collaborator.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCollaboratorPo {
  @NotBlank
  private String inviteeEmail;
  private String notebookId;
}
