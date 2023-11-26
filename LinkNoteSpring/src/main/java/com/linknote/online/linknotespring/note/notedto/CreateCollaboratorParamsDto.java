package com.linknote.online.linknotespring.note.notedto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateCollaboratorParamsDto {
  @NotBlank(message = "email不能為空")
  @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,6}$", message = "Email格式不正確")
  private String email;
  private Integer userId;

  //從url path取得
  private Integer notebookId;

  //collaboratorId不是前端傳的，而是透過NotebookService處理request時，透過email查詢userId，在set到param。
  private Integer collaboratorId;

  public Integer getCollaboratorId() {
    return collaboratorId;
  }

  public void setCollaboratorId(Integer collaboratorId) {
    this.collaboratorId = collaboratorId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

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
