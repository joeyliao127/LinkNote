package com.joeyliao.linknoteresource.invitation.po;

import com.joeyliao.linknoteresource.invitation.dto.ReceivedInvitationDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetReceivedInvitationResponsePo extends InvitationResponsePo {
  private List<ReceivedInvitationDTO> invitations;
}
