package com.joeyliao.linknoteresource.tag.po;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class CreateNotebookTagsRequestPo {
  private String tagId;
  private String notebookId;
  private List<TagPo> tags;
}
