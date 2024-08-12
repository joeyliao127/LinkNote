package com.joeyliao.linknoteresource.invitation.po;

import com.joeyliao.linknoteresource.invitation.dto.SentInvitationDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetSentInvitationResponsePo extends InvitationResponsePo{
  private List<SentInvitationDTO> invitations;

}
