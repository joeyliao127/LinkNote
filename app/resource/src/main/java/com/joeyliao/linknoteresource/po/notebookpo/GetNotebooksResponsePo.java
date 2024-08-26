package com.joeyliao.linknoteresource.po.notebookpo;

import com.joeyliao.linknoteresource.dto.notebookdto.NotebooksDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetNotebooksResponsePo {
  private List<NotebooksDTO> notebooks;
  private Boolean nextPage;
}
