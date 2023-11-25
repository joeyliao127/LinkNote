package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;

public interface NoteService {
    Integer createNote(CreateNoteParamsDto params, Integer notebookId);
    Boolean updateNote(UpdateNoteParamsDto params);
}
