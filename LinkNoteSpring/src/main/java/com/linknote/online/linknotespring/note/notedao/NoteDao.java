package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;

public interface NoteDao {
  void createNote(CreateNoteParamsDto params, Integer notebookId);

  void updateNote(UpdateNoteParamsDto params);

  void updateNoteStar(UpdateNoteStarParamDto params);

  void deleteNote(DeleteNoteParamDto param);
}
