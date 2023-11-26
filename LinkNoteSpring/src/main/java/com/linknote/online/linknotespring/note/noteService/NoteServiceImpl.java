package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NoteDao;
import com.linknote.online.linknotespring.note.notedao.UpdateIntermediaryDao;
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

  @Autowired
  UpdateIntermediaryDao updateIntermediaryDao;

  @Override
  public Integer createNote(CreateNoteParamsDto params, Integer notebookId) {
    return noteDao.createNote(params, notebookId);
  }

  @Override
  public Boolean updateNote(UpdateNoteParamsDto params) {
    Integer result = noteDao.updateNote(params);
    updateIntermediaryDao.updateNotTags(params.getTags(), params.getNoteId());


    return result == 1;

  }
}
