package com.joeyliao.linknoteresource.dao;

import com.joeyliao.linknoteresource.dto.collaboratordto.CollaboratorsDTO;
import com.joeyliao.linknoteresource.po.collaboratorpo.DeleteCollaboratorPo;
import com.joeyliao.linknoteresource.po.collaboratorpo.GetCollaboratorsRequestPo;
import java.util.List;

public interface CollaboratorDAO {

  List<CollaboratorsDTO> getCollaborators(GetCollaboratorsRequestPo po);

  void deleteCollaborator(DeleteCollaboratorPo po);

  void createCollaborator(String inviteeEmail, String notebookId);

}
