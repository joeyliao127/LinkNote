package com.linknote.online.linknotespring.note.notedao;

import java.util.List;

public interface IntermediaryDao {
  Integer updateNotebookTags(Integer notebookId, Integer tagId);
  Integer createNotTags(Integer tagId, Integer noteId);

  Integer getNoteTagPair (Integer noteId, Integer tagId);

}
