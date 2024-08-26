package com.joeyliao.linknoteresource.po.tag;

import lombok.Data;

@Data
public class DeleteNoteTagRequestPo {
  private String tagId;
  private String noteId;
}
