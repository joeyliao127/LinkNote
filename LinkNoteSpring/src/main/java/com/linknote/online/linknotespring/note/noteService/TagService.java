package com.linknote.online.linknotespring.note.noteService;

public interface TagService {

  //createTag有自動檢查是否有重複的tag，並且插入notebookTag中介表關聯資訊
  Boolean createTag(String tagName, Integer notebookId, Integer userId);

}
