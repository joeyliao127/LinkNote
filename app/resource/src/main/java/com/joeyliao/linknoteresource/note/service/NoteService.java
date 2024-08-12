package com.joeyliao.linknoteresource.note.service;

import com.joeyliao.linknoteresource.note.po.DeleteNotePo;
import com.joeyliao.linknoteresource.note.po.GetNoteRequestPo;
import com.joeyliao.linknoteresource.note.po.GetNoteResponsePo;
import com.joeyliao.linknoteresource.note.po.GetNotesResponsePo;
import com.joeyliao.linknoteresource.note.po.CreateNotePo;
import com.joeyliao.linknoteresource.note.po.GetNotesRequestPo;
import com.joeyliao.linknoteresource.note.po.updateNotePo;

public interface NoteService {

  String createNote(CreateNotePo po);

  GetNotesResponsePo getNotes(GetNotesRequestPo po);

  GetNoteResponsePo getNote(GetNoteRequestPo po);

  void updateNote(updateNotePo po);

  void deleteNote(DeleteNotePo po);
}
