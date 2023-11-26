package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboraotrsParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNotebookNameParamDto;
import com.linknote.online.linknotespring.note.notedto.QueryNotebooksParamsDto;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;

public interface NotebookService {

  NotebooksResPO getNotebooks(QueryNotebooksParamsDto params);

  void createNotebook(CreateNotebookParamsDto params, Integer userId);

  void createNotebookTag(String tag, Integer notebookId, Integer userId);

  void createCollaborator(CreateCollaboratorParamsDto params);

  void updateNotebookName(UpdateNotebookNameParamDto params);

  void deleteCollaborators(DeleteCollaboraotrsParamDto params);

}
