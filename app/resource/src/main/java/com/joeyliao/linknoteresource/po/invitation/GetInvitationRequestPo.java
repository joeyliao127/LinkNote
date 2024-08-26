package com.joeyliao.linknoteresource.po.invitation;

import com.joeyliao.linknoteresource.po.generic.PaginationPo;
import lombok.Data;

@Data
public class GetInvitationRequestPo extends PaginationPo {
  private String Authorization;
  private String userEmail;
}
