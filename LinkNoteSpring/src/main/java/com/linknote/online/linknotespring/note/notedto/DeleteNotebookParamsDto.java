package com.linknote.online.linknotespring.note.notedto;

public class DeleteNotebookParamsDto {

  private Integer userId;
  private Integer notebookId;

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getNotebookId() {
    return notebookId;
  }

  public void setNotebookId(Integer notebookId) {
    this.notebookId = notebookId;
  }
}
