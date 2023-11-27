package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import java.util.List;

public interface TagDao {
  String verifyTagExist(Integer notebookId, String tag);

  void createNotebookTag(String tag ,Integer notebookId);

  void createNotebookTags(List<String> tagList ,Integer notebookId);

  void createNoteTag(CreateNoteTagParamDto param);

  void deleteNoteTag(DeleteNoteParamDto param);



}
