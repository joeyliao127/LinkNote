package com.linknote.online.linknotespring.note.notedto;

import jakarta.validation.constraints.NotNull;

public class NotebookParamDto {
  @NotNull
  private String name;
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

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
