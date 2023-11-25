package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;

public interface NoteDao {
  Integer createNote(CreateNoteParamsDto params, Integer notebookId);
}
