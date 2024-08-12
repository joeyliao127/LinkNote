package com.joeyliao.linknoteresource.notebook.po;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateNotebookPo {

  private String notebookId;
  @NotBlank
  private String name;
  @NotBlank
  private String description;
}
