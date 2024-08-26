package com.joeyliao.linknoteresource.po.notebookpo;

import com.joeyliao.linknoteresource.po.generic.PaginationPo;
import lombok.Data;

@Data
public class GetNotebooksRequestPo extends PaginationPo {
  private String Authorization;
  private String userId;
}
