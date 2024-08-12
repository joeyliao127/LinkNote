package com.joeyliao.linknoteresource.invitation.po;

import com.joeyliao.linknoteresource.generic.po.PaginationPo;
import lombok.Data;

@Data
public class GetInvitationRequestPo extends PaginationPo {
  private String Authorization;
  private String userEmail;
}
