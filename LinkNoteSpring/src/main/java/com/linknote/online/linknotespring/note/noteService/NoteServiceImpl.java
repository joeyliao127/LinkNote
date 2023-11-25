package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NoteDao;
import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService{

  @Autowired
  NoteDao noteDao;
  @Override
  public Integer createNote(CreateNoteParamsDto params, Integer notebookId) {
    return noteDao.createNote(params, notebookId);
  }
}
