package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;

public interface NoteService {
    Integer createNote(CreateNoteParamsDto params, Integer notebookId);
}
