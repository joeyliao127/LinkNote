package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.NotebookCreateParamsDTO;
import com.linknote.online.linknotespring.note.notedto.NotebooksQueryParamsDTO;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import java.util.List;

public interface NotebookDao {

  List<NotebooksPO> getNotebooks(NotebooksQueryParamsDTO params, Boolean getCoNotebook);

  void createNotebook(NotebookCreateParamsDTO params, Integer userId);




  Integer getNotebookIdByNotebookName(String notebookName);

  void updateCollaborators(Integer notebookId, Integer userId);
  void updateNotebookTags(Integer notebookId, Integer tagId);
  void updateNoteTags(Integer noteId, Integer tagId);


}
