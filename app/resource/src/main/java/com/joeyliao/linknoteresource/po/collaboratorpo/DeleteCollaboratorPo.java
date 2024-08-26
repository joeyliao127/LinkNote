package com.joeyliao.linknoteresource.po.collaboratorpo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeleteCollaboratorPo {
  private String notebookId;
  @NotBlank
  private String userEmail;
}