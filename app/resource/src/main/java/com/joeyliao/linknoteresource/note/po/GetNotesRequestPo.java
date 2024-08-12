package com.joeyliao.linknoteresource.note.po;

import lombok.Data;

@Data
public class GetNotesRequestPo {
  private String notebookId;
  private Integer offset;
  private Integer limit;
  private String tag;
  private String star;
  private String sortDesc;
  private String keyword;
}
