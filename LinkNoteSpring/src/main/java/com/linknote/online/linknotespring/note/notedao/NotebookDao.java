package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDTO;
import com.linknote.online.linknotespring.note.notedto.QueryNotebooksParamsDTO;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import java.util.List;

public interface NotebookDao {

  List<NotebooksPO> getNotebooks(QueryNotebooksParamsDTO params, Boolean getCoNotebook);

  void createNotebook(CreateNotebookParamsDTO params, Integer userId);


  Integer getNotebookIdByNotebookName(String notebookName);

  void updateCollaborators(Integer notebookId, Integer userId);
  void updateNoteTags(Integer noteId, Integer tagId);


}
