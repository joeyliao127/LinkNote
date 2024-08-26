package com.joeyliao.linknoteresource.po.invitation;

import com.joeyliao.linknoteresource.dto.invitation.SentInvitationDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetSentInvitationResponsePo extends InvitationResponsePo{
  private List<SentInvitationDTO> invitations;

}
