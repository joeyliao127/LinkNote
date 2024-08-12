package com.joeyliao.linknoteresource.collaborator.service;

import com.joeyliao.linknoteresource.collaborator.po.CreateCollaboratorPo;
import com.joeyliao.linknoteresource.collaborator.po.DeleteCollaboratorPo;
import com.joeyliao.linknoteresource.collaborator.po.GetCollaboratorsRequestPo;
import com.joeyliao.linknoteresource.collaborator.po.GetCollaboratorsResponsePo;

public interface CollaboratorService {

  GetCollaboratorsResponsePo getCollaborators(GetCollaboratorsRequestPo po);

  void deleteCollaborator(DeleteCollaboratorPo po);

  void createCollaborator(String inviteeEmail, String notebookId);
}
