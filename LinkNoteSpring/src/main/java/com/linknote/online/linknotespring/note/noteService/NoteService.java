package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.GetTagsParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotesParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteSharedParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;
import com.linknote.online.linknotespring.note.notepo.response.NoteResPO;
import com.linknote.online.linknotespring.note.notepo.response.NotesResPO;
import com.linknote.online.linknotespring.note.notepo.response.TagResPO;

public interface NoteService {

    NotesResPO getNotes(GetNotesParamDto params);

    NoteResPO getNote(GetNoteParamDto params);

    TagResPO getNoteTags(GetTagsParamDto params);

    void createNote(CreateNoteParamsDto params);

    void createNoteTag(UpdateNoteTagParamDto params);

    void updateNote(UpdateNoteParamsDto params);

    void updateNoteStar(UpdateNoteStarParamDto params);

    void updateNoteShared(UpdateNoteSharedParamDto param);

    void deleteNote(DeleteNoteParamDto param);

    void deleteNoteTag(DeleteNoteParamDto params);
}
