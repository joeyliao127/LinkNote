package com.joeyliao.linknoteresource.po.collaboratorpo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateCollaboratorPo {
  @NotBlank
  private String inviteeEmail;
  private String notebookId;
}
