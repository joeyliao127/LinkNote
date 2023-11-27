package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NotebookDao;
import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboratorsParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookTagParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNotebookNameParamDto;
import com.linknote.online.linknotespring.note.notedto.QueryNotebooksParamsDto;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;

public interface NotebookService {

  NotebooksResPO getNotebooks(QueryNotebooksParamsDto params);

  void createNotebook(CreateNotebookParamsDto params, Integer userId);
  void createNotebookTag(String tag, Integer notebookId, Integer userId);

  void createCollaborator(CreateCollaboratorParamsDto params);

  void updateNotebookName(UpdateNotebookNameParamDto params);

  void deleteCollaborators(DeleteCollaboratorsParamDto params);

  void verifyNotebookOwnerByUserId (Integer userId, Integer notebookId, NotebookDao notebookDao );

  void deleteNotebook(DeleteNotebookParamsDto params);

  void deleteNotebookTag(DeleteNotebookTagParamDto param);
}
