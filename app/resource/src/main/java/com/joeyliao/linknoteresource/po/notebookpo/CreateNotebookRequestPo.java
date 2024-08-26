package com.joeyliao.linknoteresource.po.notebookpo;

import com.joeyliao.linknoteresource.po.tag.TagPo;
import java.util.List;
import lombok.Data;

@Data
public class CreateNotebookRequestPo {
  private String name;
  private String description;
  private String userId;
  private List<TagPo> tags;
}
