package com.joeyliao.linknoteresource.po.tag;

import com.joeyliao.linknoteresource.dto.tag.TagDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetTagResponsePo {
  private List<TagDTO> tags;
}
