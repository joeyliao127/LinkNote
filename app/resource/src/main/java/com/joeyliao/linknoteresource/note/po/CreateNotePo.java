package com.joeyliao.linknoteresource.note.po;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateNotePo {
  private String notebookId;
  private String noteId;
}
