package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.NotebookParamDto;
import com.linknote.online.linknotespring.note.notedto.QueryNotebooksParamsDto;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;

public interface NotebookService {

  NotebooksResPO getNotebooks(QueryNotebooksParamsDto params);

  void createNotebook(CreateNotebookParamsDto params, Integer userId);

  Boolean createNotebookTag(String tag, Integer notebookId, Integer userId);

  Boolean updateNotebookName(NotebookParamDto params);

}
