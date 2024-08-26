package com.joeyliao.linknoteresource.po.generic;

import lombok.Data;
import org.springframework.data.relational.core.sql.In;

@Data
public class PaginationPo {
  private Integer limit;
  private Integer offset;
  private String keyword;
  private Boolean orderByDesc;
}
