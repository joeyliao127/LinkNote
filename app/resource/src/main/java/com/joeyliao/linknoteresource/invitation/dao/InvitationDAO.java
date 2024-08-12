package com.joeyliao.linknoteresource.invitation.dao;

import com.joeyliao.linknoteresource.invitation.dto.ReceivedInvitationDTO;
import com.joeyliao.linknoteresource.invitation.dto.SentInvitationDTO;
import com.joeyliao.linknoteresource.invitation.po.CreateInvitationPo;
import com.joeyliao.linknoteresource.invitation.po.DeleteInvitationPo;
import com.joeyliao.linknoteresource.invitation.po.GetInvitationRequestPo;
import com.joeyliao.linknoteresource.invitation.po.UpdateInvitationPo;
import java.util.List;

public interface InvitationDAO {

  void createInvitation(CreateInvitationPo po);

  List<Integer> checkInvitationNotExist(CreateInvitationPo po);

  List<SentInvitationDTO> getSentInvitation(GetInvitationRequestPo po);

  List<ReceivedInvitationDTO> getReceivedInvitation(GetInvitationRequestPo po);

  void deleteInvitation(DeleteInvitationPo po);

  void updateInvitation(UpdateInvitationPo po);
}
