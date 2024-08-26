package com.joeyliao.linknoteresource.po.note;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNotePo {
  private String notebookId;
  private String noteId;
}
