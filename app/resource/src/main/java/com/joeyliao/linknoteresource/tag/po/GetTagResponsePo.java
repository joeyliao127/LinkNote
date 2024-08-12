package com.joeyliao.linknoteresource.tag.po;

import com.joeyliao.linknoteresource.tag.dto.TagDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetTagResponsePo {
  private List<TagDTO> tags;
}
