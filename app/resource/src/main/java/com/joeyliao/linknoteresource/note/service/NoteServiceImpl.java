package com.joeyliao.linknoteresource.note.service;

import com.joeyliao.linknoteresource.generic.enums.Target;
import com.joeyliao.linknoteresource.generic.uuidgenerator.service.UUIDGeneratorService;
import com.joeyliao.linknoteresource.note.dao.NoteDAO;
import com.joeyliao.linknoteresource.note.dto.NoteDTO;
import com.joeyliao.linknoteresource.note.po.CreateNotePo;
import com.joeyliao.linknoteresource.note.po.DeleteNotePo;
import com.joeyliao.linknoteresource.note.po.GetNoteRequestPo;
import com.joeyliao.linknoteresource.note.po.GetNoteResponsePo;
import com.joeyliao.linknoteresource.note.po.GetNotesRequestPo;
import com.joeyliao.linknoteresource.note.po.GetNotesResponsePo;
import com.joeyliao.linknoteresource.note.po.updateNotePo;
import com.joeyliao.linknoteresource.notebook.dao.NotebookDAO;
import com.joeyliao.linknoteresource.tag.dao.TagDAO;
import com.joeyliao.linknoteresource.tag.service.TagService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NoteServiceImpl implements NoteService {

  @Autowired
  NoteDAO noteDAO;

  @Autowired
  TagDAO tagDAO;

  @Autowired
  NotebookDAO notebookDAO;

  @Autowired
  UUIDGeneratorService uuidGeneratorService;

  @Override
  public String createNote(CreateNotePo po) {
    po.setNoteId(uuidGeneratorService.generateUUID(Target.NOTE));
    noteDAO.createNotes(po);
    return po.getNoteId();
  }

  @Override
  public GetNotesResponsePo getNotes(GetNotesRequestPo po) {
    //limit加1是為了判斷若query後的資料比數跟limit + 1一樣，代表有nextPage
    // 因此返回notes資料時要移除最後一項。
    po.setLimit(po.getLimit() + 1);
    GetNotesResponsePo responsePo = new GetNotesResponsePo();
    List<NoteDTO> list = noteDAO.getNotes(po);
    if(list.size() == po.getLimit()){
      responsePo.setNextPage(true);
      list.remove(list.size() - 1);
    }else{
      responsePo.setNextPage(false);
    }
    responsePo.setNotes(list);
    responsePo.setTags(tagDAO.getNotebookTags(po.getNotebookId()));
    responsePo.setNotebookName(notebookDAO.getNotebookName(po.getNotebookId()));
    return responsePo;
  }

  @Override
  public GetNoteResponsePo getNote(GetNoteRequestPo po) {
    GetNoteResponsePo responsePo = new GetNoteResponsePo();
    responsePo.setNote(noteDAO.getNote(po));
    responsePo.setTags(tagDAO.getNoteTags(po.getNoteId()));
    return responsePo;
  }

  @Override
  public void updateNote(updateNotePo po) {
    noteDAO.updateNote(po);
  }

  @Override
  public void deleteNote(DeleteNotePo po) {
    noteDAO.deleteNote(po);
  }
}
