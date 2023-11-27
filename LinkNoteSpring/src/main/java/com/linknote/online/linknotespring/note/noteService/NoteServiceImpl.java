package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NoteDao;
import com.linknote.online.linknotespring.note.notedao.IntermediaryDao;
import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService{

  @Autowired
  NoteDao noteDao;

  @Autowired
  TagService tagService;

  @Autowired
  IntermediaryDao intermediaryDao;

  @Override
  public void createNote(CreateNoteParamsDto params, Integer notebookId) {
    noteDao.createNote(params, notebookId);
  }

  @Override
  public void createNoteTag(CreateNoteTagParamDto params) {
    tagService.createNoteTag(params);
  }

  @Override
  public void updateNote(UpdateNoteParamsDto params) {
    noteDao.updateNote(params);
  }

  @Override
  public void updateNoteStar(UpdateNoteStarParamDto params) {
    noteDao.updateNoteStar(params);
  }

  @Override
  public void deleteNote(DeleteNoteParamDto param) {
    noteDao.deleteNote(param);
  }

  @Override
  public void deleteNoteTag(DeleteNoteParamDto params) {
    tagService.deleteNoteTag(params);
  }
}
