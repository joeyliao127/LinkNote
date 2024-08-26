package com.joeyliao.linknoteresource.po.collaboratorpo;

import com.joeyliao.linknoteresource.dto.collaboratordto.CollaboratorsDTO;
import java.util.List;
import lombok.Data;

@Data
public class GetCollaboratorsResponsePo {
  private List<CollaboratorsDTO> collaborators;
  private NotebookOwnerDTO owner;
}
