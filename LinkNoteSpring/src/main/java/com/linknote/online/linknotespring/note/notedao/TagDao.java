package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.UpdateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import java.util.List;

public interface TagDao {
  String verifyTagExist(Integer notebookId, String tag);

  List<String> getTags(Integer userId, Integer notebookId);
  List<String> getTags(Integer userId, Integer notebookId, Integer noteId);

  void createNotebookTag(String tag ,Integer notebookId);

  void createNotebookTags(List<String> tagList ,Integer notebookId);

  void createNoteTag(UpdateNoteTagParamDto param);

  void deleteNoteTag(DeleteNoteParamDto param);



}
