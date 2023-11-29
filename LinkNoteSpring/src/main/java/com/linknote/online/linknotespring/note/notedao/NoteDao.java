package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotesParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteSharedParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;
import com.linknote.online.linknotespring.note.notepo.po.NotePO;
import com.linknote.online.linknotespring.note.notepo.response.NoteResPO;
import java.util.List;

public interface NoteDao {

  List<NotePO> getNotes(GetNotesParamDto params);

  Integer getNoteIdByNameForVerifyNameExist(String name, Integer notebookId);
  void createNote(CreateNoteParamsDto params);

  void updateNote(UpdateNoteParamsDto params);

  void updateNoteStar(UpdateNoteStarParamDto params);

  void updateNoteShared(UpdateNoteSharedParamDto params);

  void deleteNote(DeleteNoteParamDto param);
}
