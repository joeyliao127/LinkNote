package com.joeyliao.linknoteresource.collaborator.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteCollaboratorPo {
  private String notebookId;
  @NotBlank
  private String userEmail;
}
