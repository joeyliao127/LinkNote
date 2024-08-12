package com.joeyliao.linknoteresource.tag.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNotebookTagRequestPo {
  private String notebookId;
  private String tagId;
  @NotBlank
  private String name;
}
