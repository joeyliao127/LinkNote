package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.CreateTagParamDto;
import java.util.List;

public interface TagService {

  //createTag有自動檢查是否有重複的tag，並且插入notebookTag中介表關聯資訊
  Boolean createNotebookTag(String tagName, Integer notebookId, Integer userId);

  Boolean createNoteTag(CreateTagParamDto params);


}
