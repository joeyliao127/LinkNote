package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboratorsParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookTagParamDto;
import java.util.List;

public interface IntermediaryDao {

  Integer getCollaboratorsCount(Integer ownerId, Integer notebookId);

  void createNotebookCollaborators(List<Integer> collaboratorList, Integer notebookId, Integer ownerId);

  void createCollaborator(CreateCollaboratorParamsDto params);

  void deleteCollaborator(DeleteCollaboratorsParamDto params);

  void deleteCollaborators(DeleteNotebookParamsDto params);


}
