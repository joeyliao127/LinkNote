package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.CreateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import java.util.List;

public interface TagService {

  //單筆新增tag
  void createNotebookTag(String tagName, Integer notebookId);

  //批量新增tag，用於建立notebook時。
  void createNotebookTag(List<String> tagList, Integer notebookId);

  void createNoteTag(CreateNoteTagParamDto params);

  void deleteNoteTag(DeleteNoteParamDto param);





}
