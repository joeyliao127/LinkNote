package com.linknote.online.linknotespring.note.notedto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateNoteParamsDto {

  private String noteName;

  private Integer notebookId;

  public Integer getNotebookId() {
    return notebookId;
  }

  public void setNotebookId(Integer notebookId) {
    this.notebookId = notebookId;
  }

  public String getNoteName() {
    return noteName;
  }

  public void setNoteName(String noteName) {
    this.noteName = noteName;
  }
}
