package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboraotrsParamDto;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.util.List;

public interface IntermediaryService {

  Integer getNoteTagPair (Integer noteId, Integer tagId);

  Boolean verifyCollaboratorsCount(Integer ownerId, Integer notebookId);

  //建立notebook時，批量新增協作者
  void createNotebookCollaborators(List<Integer> collaboratorList, Integer notebookId, Integer ownerId);

  void createNotebookCollaborator(CreateCollaboratorParamsDto params);

  void deleteCollaborators (DeleteCollaboraotrsParamDto param);

  void updateNotebookTags(Integer notebookId, Integer tagId);

  void createNotTags(Integer tagId, Integer noteId);



}
