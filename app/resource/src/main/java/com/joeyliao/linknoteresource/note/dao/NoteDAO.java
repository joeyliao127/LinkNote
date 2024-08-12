package com.joeyliao.linknoteresource.note.dao;

import com.joeyliao.linknoteresource.collaborator.po.NotebookOwnerDTO;
import com.joeyliao.linknoteresource.note.dto.NoteDTO;
import com.joeyliao.linknoteresource.note.po.CreateNotePo;
import com.joeyliao.linknoteresource.note.po.DeleteNotePo;
import com.joeyliao.linknoteresource.note.po.GetNoteRequestPo;
import com.joeyliao.linknoteresource.note.po.GetNotesRequestPo;
import com.joeyliao.linknoteresource.note.po.GetNotesResponsePo;
import com.joeyliao.linknoteresource.note.po.updateNotePo;
import java.util.List;

public interface NoteDAO {

  void createNotes(CreateNotePo po);

  List<NoteDTO> getNotes(GetNotesRequestPo po);

  NoteDTO getNote(GetNoteRequestPo po);

  void updateNote(updateNotePo po);

  void deleteNote(DeleteNotePo po);

}
