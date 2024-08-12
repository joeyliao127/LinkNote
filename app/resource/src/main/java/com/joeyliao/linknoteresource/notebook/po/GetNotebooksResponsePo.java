package com.joeyliao.linknoteresource.notebook.po;

import com.joeyliao.linknoteresource.notebook.dto.NotebooksDTO;
import com.joeyliao.linknoteresource.tag.dto.TagDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetNotebooksResponsePo {
  private List<NotebooksDTO> notebooks;
  private Boolean nextPage;
}
