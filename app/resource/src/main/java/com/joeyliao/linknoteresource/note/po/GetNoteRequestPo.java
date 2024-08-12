package com.joeyliao.linknoteresource.note.po;

import lombok.Data;

@Data
public class GetNoteRequestPo {
  private String notebookId;
  private String noteId;
}
