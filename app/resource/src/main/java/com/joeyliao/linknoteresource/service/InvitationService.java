package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.po.invitation.CreateInvitationPo;
import com.joeyliao.linknoteresource.po.invitation.DeleteInvitationPo;
import com.joeyliao.linknoteresource.po.invitation.GetInvitationRequestPo;
import com.joeyliao.linknoteresource.po.invitation.GetReceivedInvitationResponsePo;
import com.joeyliao.linknoteresource.po.invitation.GetSentInvitationResponsePo;
import com.joeyliao.linknoteresource.po.invitation.UpdateInvitationPo;
import org.apache.coyote.BadRequestException;

public interface InvitationService {

  void createInvitation(CreateInvitationPo po) throws BadRequestException;

  GetSentInvitationResponsePo getSentInvitation(GetInvitationRequestPo po);

  GetReceivedInvitationResponsePo getReceivedInvitation(GetInvitationRequestPo po);

  void deleteInvitation(DeleteInvitationPo po);

  void updateInvitation(UpdateInvitationPo po);
}
