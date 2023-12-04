package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.GetTagsParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notepo.response.TagResPO;
import java.util.List;

public interface TagService {

  TagResPO getTags(GetTagsParamDto params);
  //單筆新增tag
  void createNotebookTag(String tagName, Integer notebookId);

  //批量新增tag，用於建立notebook時。
  void createNotebookTag(List<String> tagList, Integer notebookId);

  void updateNoteTag(UpdateNoteTagParamDto params);






}
