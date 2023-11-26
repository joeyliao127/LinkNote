package com.linknote.online.linknotespring.note.notedao;

public interface TagDao {

  Integer getTagIdByTagNameAndNotebookId(Integer notebookId, String tag);

  Integer getTagIdByTagName(String tag);

  Integer createTag(String tag);



}
