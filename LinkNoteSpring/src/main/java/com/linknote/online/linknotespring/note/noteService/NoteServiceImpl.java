package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NoteDao;
import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.GetTagsParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotesParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteSharedParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;
import com.linknote.online.linknotespring.note.noteexception.NoteAlreadyExistException;
import com.linknote.online.linknotespring.note.notepo.po.NotePO;
import com.linknote.online.linknotespring.note.notepo.po.NotesPO;
import com.linknote.online.linknotespring.note.notepo.response.NoteResPO;
import com.linknote.online.linknotespring.note.notepo.response.NotesResPO;
import com.linknote.online.linknotespring.note.notepo.response.TagResPO;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService{

  @Autowired
  NoteDao noteDao;

  @Autowired
  TagService tagService;


  @Override
  public NotesResPO getNotes(GetNotesParamDto params) {
    List<NotesPO> notes = noteDao.getNotes(params);
    NotesResPO responsePO = new NotesResPO();
    System.out.println("取得的notes長度：" + notes.size());
    if(notes.size() <= params.getLimit() & !notes.isEmpty()){
      responsePO.setNextPage(false);
    }else if(notes.size() > params.getLimit()){
      notes.remove(notes.size() - 1);
      responsePO.setNextPage(true);
    }else if(notes.isEmpty()){
      responsePO.setNextPage(false);
    }
    responsePO.setResult(true);
    responsePO.setNotes(notes);
    return responsePO;
  }

  @Override
  public NoteResPO getNote(GetNoteParamDto param) {
    NotePO notePO = noteDao.getNote(param);
    NoteResPO noteResPO = new NoteResPO();
    noteResPO.setResult(true);
    noteResPO.setNotePO(notePO);
    return noteResPO;
  }

  @Override
  public TagResPO getNoteTags(GetTagsParamDto params) {
    return tagService.getTags(params);
  }

  @Override
  public void createNote(CreateNoteParamsDto params) {
    Integer result = noteDao.getNoteIdByNameForVerifyNameExist(params.getNoteName(), params.getNotebookId());
    if(result != null){
      throw new NoteAlreadyExistException("NoteService: note name already exist ");
    }
    noteDao.createNote(params);
  }

  @Override
  public void createNoteTag(UpdateNoteTagParamDto params) {
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
