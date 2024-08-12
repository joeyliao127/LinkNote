package com.joeyliao.linknoteresource.notebook.po;

import com.joeyliao.linknoteresource.tag.po.TagPo;
import java.util.List;
import lombok.Data;

@Data
public class CreateNotebookRequestPo {
  private String name;
  private String description;
  private String userId;
  private List<TagPo> tags;
}
