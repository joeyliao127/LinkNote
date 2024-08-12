package com.joeyliao.linknoteresource.collaborator.dao;

import com.joeyliao.linknoteresource.collaborator.dto.CollaboratorsDTO;
import com.joeyliao.linknoteresource.collaborator.po.DeleteCollaboratorPo;
import com.joeyliao.linknoteresource.collaborator.po.GetCollaboratorsRequestPo;
import java.util.List;

public interface CollaboratorDAO {

  List<CollaboratorsDTO> getCollaborators(GetCollaboratorsRequestPo po);

  void deleteCollaborator(DeleteCollaboratorPo po);

  void createCollaborator(String inviteeEmail, String notebookId);

}
