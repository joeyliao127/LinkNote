package com.linknote.online.linknotespring.note.notedao;

public interface TagDao {
  Integer createTag(String tag);
  Integer getTagIdByTagName(String tag);
  void addTagByUserId(Integer id);
}
