package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDTO;
import com.linknote.online.linknotespring.note.notedto.QueryNotebooksParamsDTO;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;

public interface NotebookService {

  NotebooksResPO getNotebooks(QueryNotebooksParamsDTO params);

  void createNotebook(CreateNotebookParamsDTO params, Integer userId);



}
