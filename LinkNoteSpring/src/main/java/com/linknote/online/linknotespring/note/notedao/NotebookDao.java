package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.QueryNotebooksParamsDto;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import java.util.List;

public interface NotebookDao {

  List<NotebooksPO> getNotebooks(QueryNotebooksParamsDto params, Boolean getCoNotebook);

  void createNotebook(CreateNotebookParamsDto params, Integer userId);

  //For verify notebook owner
  Integer getNotebookIdByUserId(Integer userId, Integer notebookId);
  Integer getNotebookIdByNotebookName(String notebookName);

  void updateCollaborators(Integer notebookId, Integer userId);
  void updateNoteTags(Integer noteId, Integer tagId);


}
