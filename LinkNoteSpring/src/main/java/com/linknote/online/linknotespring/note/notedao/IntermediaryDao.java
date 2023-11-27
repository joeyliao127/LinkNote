package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboratorsParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookTagParamDto;
import java.util.List;

public interface IntermediaryDao {

  Integer getNoteTagPair (Integer noteId, Integer tagId);

  Integer getCollaboratorsCount(Integer ownerId, Integer notebookId);

  void createNotTags(Integer tagId, Integer noteId);

  void createNotebookCollaborators(List<Integer> collaboratorList, Integer notebookId, Integer ownerId);

  void createCollaborator(CreateCollaboratorParamsDto params);


  void updateNotebookTags(Integer notebookId, Integer tagId);

  void deleteCollaborator(DeleteCollaboratorsParamDto params);

  void deleteCollaborators(DeleteNotebookParamsDto params);

  void deleteNotebooksTag(DeleteNotebookTagParamDto param);

  void deleteNotebooksTags(DeleteNotebookParamsDto params);

}
