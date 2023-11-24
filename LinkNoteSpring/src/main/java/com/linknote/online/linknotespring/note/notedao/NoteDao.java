package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.NotebooksQueryParams;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import java.util.List;

public interface NoteDao {

  List<NotebooksPO> getNotebooks(NotebooksQueryParams params, Boolean getCoNotebook);



}
