package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.UpdateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notepo.po.TagPO;
import java.util.List;

public interface TagDao {
  String verifyTagExist(Integer notebookId, String tag);

  List<TagPO> getNotebookTags(Integer userId, Integer notebookId);
  List<TagPO> getNoteTags(Integer noteId);

  void createNotebookTag(String tag ,Integer notebookId);

  void createNotebookTags(List<String> tagList ,Integer notebookId);

  void createNoteTags(UpdateNoteTagParamDto param);

  void deleteNoteTags(Integer noteId);



}
