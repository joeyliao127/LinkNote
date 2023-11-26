package com.linknote.online.linknotespring.note.notedto;

import io.swagger.v3.oas.models.security.SecurityScheme.In;

public class DeleteCollaboraotrsParamDto {

  private Integer notebookId;
  private Integer userId;
  private Integer collaboratorId;

  public Integer getCollaboratorId() {
    return collaboratorId;
  }

  public void setCollaboratorId(Integer collaboratorId) {
    this.collaboratorId = collaboratorId;
  }

  public Integer getNotebookId() {
    return notebookId;
  }

  public void setNotebookId(Integer notebookId) {
    this.notebookId = notebookId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }
}
