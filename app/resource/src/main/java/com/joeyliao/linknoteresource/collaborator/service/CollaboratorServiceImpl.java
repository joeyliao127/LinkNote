package com.joeyliao.linknoteresource.collaborator.service;

import com.joeyliao.linknoteresource.collaborator.dao.CollaboratorDAO;
import com.joeyliao.linknoteresource.collaborator.dto.CollaboratorsDTO;
import com.joeyliao.linknoteresource.collaborator.po.DeleteCollaboratorPo;
import com.joeyliao.linknoteresource.collaborator.po.GetCollaboratorsRequestPo;
import com.joeyliao.linknoteresource.collaborator.po.GetCollaboratorsResponsePo;
import com.joeyliao.linknoteresource.collaborator.po.NotebookOwnerDTO;
import com.joeyliao.linknoteresource.notebook.dao.NotebookDAO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CollaboratorServiceImpl implements CollaboratorService {

  @Autowired
  CollaboratorDAO collaboratorDAO;

  @Autowired
  NotebookDAO notebookDAO;

  @Override
  public GetCollaboratorsResponsePo getCollaborators(GetCollaboratorsRequestPo po) {
    GetCollaboratorsResponsePo responsePo = new GetCollaboratorsResponsePo();
    List<CollaboratorsDTO> list = collaboratorDAO.getCollaborators(po);
    NotebookOwnerDTO dto = notebookDAO.getNotebookOwner(po.getNotebookId());
    responsePo.setCollaborators(list);
    responsePo.setOwner(dto);
    return responsePo;
  }

  @Override
  public void deleteCollaborator(DeleteCollaboratorPo po) {
    collaboratorDAO.deleteCollaborator(po);
  }

  @Override
  public void createCollaborator(String inviteeEmail, String notebookId) {
    collaboratorDAO.createCollaborator(inviteeEmail, notebookId);
  }
}
