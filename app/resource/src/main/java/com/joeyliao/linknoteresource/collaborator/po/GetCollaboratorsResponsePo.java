package com.joeyliao.linknoteresource.collaborator.po;

import com.joeyliao.linknoteresource.collaborator.dto.CollaboratorsDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetCollaboratorsResponsePo {
  private List<CollaboratorsDTO> collaborators;
  private NotebookOwnerDTO owner;
}
