package com.joeyliao.linknoteresource.generic.po;

import lombok.Data;
import org.springframework.data.relational.core.sql.In;

@Data
public class PaginationPo {
  private Integer limit;
  private Integer offset;
  private String keyword;
  private Boolean orderByDesc;
}
