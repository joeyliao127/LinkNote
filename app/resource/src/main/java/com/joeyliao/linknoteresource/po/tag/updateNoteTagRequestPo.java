package com.joeyliao.linknoteresource.po.tag;

import lombok.Data;

@Data
public class updateNoteTagRequestPo {
  private String noteId;
  private String notebookId;
  private String tagId;
  private Boolean isCreated;


}
