package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NoteDao;
import com.linknote.online.linknotespring.note.notedao.IntermediaryDao;
import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteSharedParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;
import com.linknote.online.linknotespring.note.noteexception.NoteAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService{

  @Autowired
  NoteDao noteDao;

  @Autowired
  TagService tagService;



  @Override
  public void createNote(CreateNoteParamsDto params) {
    Integer result = noteDao.getNoteIdByNameForVerifyNameExist(params.getNoteName(), params.getNotebookId());
    if(result != null){
      throw new NoteAlreadyExistException("NoteService: note name already exist ");
    }
    noteDao.createNote(params);
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
  public void updateNoteShared(UpdateNoteSharedParamDto param) {
    noteDao.updateNoteShared(param);
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
