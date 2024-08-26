package com.joeyliao.linknoteresource.po.tag;

import java.util.List;
import lombok.Data;

@Data
public class CreateNotebookTagsRequestPo {
  private String tagId;
  private String notebookId;
  private List<TagPo> tags;
}
