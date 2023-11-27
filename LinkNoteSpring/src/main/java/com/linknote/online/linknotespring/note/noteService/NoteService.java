package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;

public interface NoteService {
    void createNote(CreateNoteParamsDto params, Integer notebookId);

    void createNoteTag(CreateNoteTagParamDto params);

    void updateNote(UpdateNoteParamsDto params);

    void updateNoteStar(UpdateNoteStarParamDto params);

    void deleteNote(DeleteNoteParamDto param);

    void deleteNoteTag(DeleteNoteParamDto params);
}
