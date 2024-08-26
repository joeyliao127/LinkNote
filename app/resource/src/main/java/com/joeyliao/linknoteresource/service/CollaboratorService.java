package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.po.collaboratorpo.DeleteCollaboratorPo;
import com.joeyliao.linknoteresource.po.collaboratorpo.GetCollaboratorsRequestPo;
import com.joeyliao.linknoteresource.po.collaboratorpo.GetCollaboratorsResponsePo;

public interface CollaboratorService {

  GetCollaboratorsResponsePo getCollaborators(GetCollaboratorsRequestPo po);

  void deleteCollaborator(DeleteCollaboratorPo po);

  void createCollaborator(String inviteeEmail, String notebookId);
}
