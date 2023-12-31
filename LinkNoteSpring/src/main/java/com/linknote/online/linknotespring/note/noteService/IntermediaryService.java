package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboratorsParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookParamsDto;
import java.util.List;

public interface IntermediaryService {

//  Integer getNoteTagPair (Integer noteId, Integer tagId);

  Boolean verifyCollaboratorsCount(Integer ownerId, Integer notebookId);

  //建立notebook時，批量新增協作者
  void createNotebookCollaborators(List<Integer> collaboratorList, Integer notebookId, Integer ownerId);

  Integer createNotebookCollaborator(CreateCollaboratorParamsDto params);

  void deleteCollaborator(DeleteCollaboratorsParamDto param);

  void deleteCollaborators(DeleteNotebookParamsDto params);


}
