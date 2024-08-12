package com.joeyliao.linknoteresource.notebook.po;

import com.joeyliao.linknoteresource.generic.po.PaginationPo;
import lombok.Data;

@Data
public class GetNotebooksRequestPo extends PaginationPo {
  private String Authorization;
  private String userId;
}
