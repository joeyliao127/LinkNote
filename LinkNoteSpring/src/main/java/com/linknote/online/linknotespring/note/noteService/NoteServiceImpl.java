package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NoteDao;
import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService{

  @Autowired
  NoteDao noteDao;

  @Autowired
  TagService tagService;

  @Override
  public Integer createNote(CreateNoteParamsDto params, Integer notebookId) {
    return noteDao.createNote(params, notebookId);
  }

  @Override
  public Boolean updateNote(UpdateNoteParamsDto params) {
    Integer result = noteDao.updateNote(params);
    tagService.createNoteTag(params.getTags(), params.getNoteId(), params.getUserId());
    return result == 1;

  }
}
