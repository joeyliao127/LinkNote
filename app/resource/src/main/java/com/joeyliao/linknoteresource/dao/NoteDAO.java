package com.joeyliao.linknoteresource.dao;

import com.joeyliao.linknoteresource.dto.note.NoteDTO;
import com.joeyliao.linknoteresource.po.note.CreateNotePo;
import com.joeyliao.linknoteresource.po.note.DeleteNotePo;
import com.joeyliao.linknoteresource.po.note.GetNoteRequestPo;
import com.joeyliao.linknoteresource.po.note.GetNotesRequestPo;
import com.joeyliao.linknoteresource.po.note.updateNotePo;
import java.util.List;

public interface NoteDAO {

  void createNotes(CreateNotePo po);

  List<NoteDTO> getNotes(GetNotesRequestPo po);

  NoteDTO getNote(GetNoteRequestPo po);

  void updateNote(updateNotePo po);

  void deleteNote(DeleteNotePo po);

}
