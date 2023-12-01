package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotesParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteSharedParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;
import com.linknote.online.linknotespring.note.notepo.po.NotePO;
import com.linknote.online.linknotespring.note.notepo.po.NotesPO;
import java.util.List;

public interface NoteDao {

  NotePO getNote(GetNoteParamDto param);
  Integer getNoteIdByNameForVerifyNameExist(String name, Integer notebookId);
  Integer createNote(CreateNoteParamsDto params);

  void updateNote(UpdateNoteParamsDto params);

  void updateNoteStar(UpdateNoteStarParamDto params);

  void updateNoteShared(UpdateNoteSharedParamDto params);

  void deleteNote(DeleteNoteParamDto param);
}
