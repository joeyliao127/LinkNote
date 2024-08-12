package com.joeyliao.linknoteresource.tag.po;

import lombok.Data;

@Data
public class updateNoteTagRequestPo {
  private String noteId;
  private String notebookId;
  private String tagId;
  private Boolean isCreated;


}
