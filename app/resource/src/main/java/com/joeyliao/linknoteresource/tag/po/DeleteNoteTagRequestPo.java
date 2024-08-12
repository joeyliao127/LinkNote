package com.joeyliao.linknoteresource.tag.po;

import lombok.Data;

@Data
public class DeleteNoteTagRequestPo {
  private String tagId;
  private String noteId;
}
