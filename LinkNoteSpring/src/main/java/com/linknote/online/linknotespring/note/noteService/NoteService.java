package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.NotebooksQueryParams;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;
import java.util.List;

public interface NoteService {
  //====================測試用(上)====================
  NotebooksResPO getNotebooks(NotebooksQueryParams params);

  //====================測試用(下)====================
}
