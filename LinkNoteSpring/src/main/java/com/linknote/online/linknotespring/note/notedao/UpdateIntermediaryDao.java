package com.linknote.online.linknotespring.note.notedao;

import java.util.List;

public interface UpdateIntermediaryDao {
  Integer updateNotebookTags(Integer notebookId, Integer tagId);
  Integer updateNotTags(List<Integer> tags, Integer noteId);

}
