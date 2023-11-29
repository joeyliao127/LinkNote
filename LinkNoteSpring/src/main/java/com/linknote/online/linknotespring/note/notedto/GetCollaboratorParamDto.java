package com.linknote.online.linknotespring.note.notedto;

public class GetCollaboratorParamDto {
  private Integer notebookId;
  private Integer userId;

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
