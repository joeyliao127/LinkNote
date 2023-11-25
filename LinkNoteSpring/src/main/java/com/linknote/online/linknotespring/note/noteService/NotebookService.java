package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.NotebookCreateParamsDTO;
import com.linknote.online.linknotespring.note.notedto.NotebooksQueryParamsDTO;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;

public interface NotebookService {

  NotebooksResPO getNotebooks(NotebooksQueryParamsDTO params);

  void createNotebook(NotebookCreateParamsDTO params, Integer userId);



}
