package com.joeyliao.linknoteresource.po.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNoteTagRequestPo {
  private String noteId;
  @NotBlank
  private String tagId;
}
