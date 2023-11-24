package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import java.util.List;

public interface NoteDao {

  List<NotebooksPO> getNotebooks(int offset, int userId);



}
