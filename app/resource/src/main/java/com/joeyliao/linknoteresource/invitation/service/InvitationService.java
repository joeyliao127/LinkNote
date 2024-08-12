package com.joeyliao.linknoteresource.invitation.service;

import com.joeyliao.linknoteresource.invitation.po.CreateInvitationPo;
import com.joeyliao.linknoteresource.invitation.po.DeleteInvitationPo;
import com.joeyliao.linknoteresource.invitation.po.GetInvitationRequestPo;
import com.joeyliao.linknoteresource.invitation.po.GetReceivedInvitationResponsePo;
import com.joeyliao.linknoteresource.invitation.po.GetSentInvitationResponsePo;
import com.joeyliao.linknoteresource.invitation.po.UpdateInvitationPo;
import org.apache.coyote.BadRequestException;

public interface InvitationService {

  void createInvitation(CreateInvitationPo po) throws BadRequestException;

  GetSentInvitationResponsePo getSentInvitation(GetInvitationRequestPo po);

  GetReceivedInvitationResponsePo getReceivedInvitation(GetInvitationRequestPo po);

  void deleteInvitation(DeleteInvitationPo po);

  void updateInvitation(UpdateInvitationPo po);
}
