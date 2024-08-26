package com.joeyliao.linknoteresource.po.note;

import lombok.Data;

@Data
public class GetNoteRequestPo {
  private String notebookId;
  private String noteId;
}
