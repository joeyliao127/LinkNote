package com.linknote.online.linknotespring.note.notedto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateNoteParamsDto {
  @NotBlank
  private String noteName;

  public String getNoteName() {
    return noteName;
  }

  public void setNoteName(String noteName) {
    this.noteName = noteName;
  }
}
