package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.po.note.DeleteNotePo;
import com.joeyliao.linknoteresource.po.note.GetNoteRequestPo;
import com.joeyliao.linknoteresource.po.note.GetNoteResponsePo;
import com.joeyliao.linknoteresource.po.note.GetNotesResponsePo;
import com.joeyliao.linknoteresource.po.note.CreateNotePo;
import com.joeyliao.linknoteresource.po.note.GetNotesRequestPo;
import com.joeyliao.linknoteresource.po.note.updateNotePo;

public interface NoteService {

  String createNote(CreateNotePo po);

  GetNotesResponsePo getNotes(GetNotesRequestPo po);

  GetNoteResponsePo getNote(GetNoteRequestPo po);

  void updateNote(updateNotePo po);

  void deleteNote(DeleteNotePo po);
}
