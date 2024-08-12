package com.joeyliao.linknoteresource.tag.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNoteTagRequestPo {
  private String noteId;
  @NotBlank
  private String tagId;
}
