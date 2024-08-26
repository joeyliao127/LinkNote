package com.joeyliao.linknoteresource.po.invitation;

import com.joeyliao.linknoteresource.dto.invitation.ReceivedInvitationDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetReceivedInvitationResponsePo extends InvitationResponsePo {
  private List<ReceivedInvitationDTO> invitations;
}
