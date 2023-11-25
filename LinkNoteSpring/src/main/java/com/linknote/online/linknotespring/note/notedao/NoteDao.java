package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;

public interface NoteDao {
  Integer createNote(CreateNoteParamsDto params, Integer notebookId);
  Integer updateNote(UpdateNoteParamsDto params);
}
