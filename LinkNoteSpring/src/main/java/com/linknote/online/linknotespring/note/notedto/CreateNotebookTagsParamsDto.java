package com.linknote.online.linknotespring.note.notedto;

import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public class CreateNotebookTagsParamsDto {
  @NotBlank
  private String tag;

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }
}
