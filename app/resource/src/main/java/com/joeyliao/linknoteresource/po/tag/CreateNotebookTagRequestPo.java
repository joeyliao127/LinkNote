package com.joeyliao.linknoteresource.po.tag;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNotebookTagRequestPo {
  private String notebookId;
  private String tagId;
  @NotBlank
  private String name;
}
