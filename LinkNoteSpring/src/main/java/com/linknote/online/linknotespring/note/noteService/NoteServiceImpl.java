package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NoteDao;
import com.linknote.online.linknotespring.note.notedto.CreateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.GetTagsParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNoteParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteSharedParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteStarParamDto;
import com.linknote.online.linknotespring.note.noteexception.NoteAlreadyExistException;
import com.linknote.online.linknotespring.note.notepo.po.NotePO;
import com.linknote.online.linknotespring.note.notepo.response.NoteResPO;
import com.linknote.online.linknotespring.note.notepo.response.TagResPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService{

  @Autowired
  NoteDao noteDao;

  @Autowired
  TagService tagService;

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
  public Integer createNote(CreateNoteParamsDto params) {
    params.setNoteName("new note");
    return noteDao.createNote(params);
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
