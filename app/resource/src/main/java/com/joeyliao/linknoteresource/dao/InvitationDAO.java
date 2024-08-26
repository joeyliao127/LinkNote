package com.joeyliao.linknoteresource.dao;

import com.joeyliao.linknoteresource.dto.invitation.ReceivedInvitationDTO;
import com.joeyliao.linknoteresource.dto.invitation.SentInvitationDTO;
import com.joeyliao.linknoteresource.po.invitation.CreateInvitationPo;
import com.joeyliao.linknoteresource.po.invitation.DeleteInvitationPo;
import com.joeyliao.linknoteresource.po.invitation.GetInvitationRequestPo;
import com.joeyliao.linknoteresource.po.invitation.UpdateInvitationPo;
import java.util.List;

public interface InvitationDAO {

  void createInvitation(CreateInvitationPo po);

  List<Integer> checkInvitationNotExist(CreateInvitationPo po);

  List<SentInvitationDTO> getSentInvitation(GetInvitationRequestPo po);

  List<ReceivedInvitationDTO> getReceivedInvitation(GetInvitationRequestPo po);

  void deleteInvitation(DeleteInvitationPo po);

  void updateInvitation(UpdateInvitationPo po);
}
