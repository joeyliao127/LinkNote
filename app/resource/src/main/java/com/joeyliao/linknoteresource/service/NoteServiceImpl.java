package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.enums.Target;
import com.joeyliao.linknoteresource.dao.NoteDAO;
import com.joeyliao.linknoteresource.dto.note.NoteDTO;
import com.joeyliao.linknoteresource.po.note.CreateNotePo;
import com.joeyliao.linknoteresource.po.note.DeleteNotePo;
import com.joeyliao.linknoteresource.po.note.GetNoteRequestPo;
import com.joeyliao.linknoteresource.po.note.GetNoteResponsePo;
import com.joeyliao.linknoteresource.po.note.GetNotesRequestPo;
import com.joeyliao.linknoteresource.po.note.GetNotesResponsePo;
import com.joeyliao.linknoteresource.po.note.updateNotePo;
import com.joeyliao.linknoteresource.dao.NotebookDAO;
import com.joeyliao.linknoteresource.dto.notebookdto.NotebooksDTO;
import com.joeyliao.linknoteresource.dao.TagDAO;
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
    NotebooksDTO dto = notebookDAO.getNotebook(po.getNotebookId());
    responsePo.setName(dto.getName());
    responsePo.setDescription(dto.getDescription());
    responsePo.setId(dto.getId());
    return responsePo;
  }

  @Override
  public GetNoteResponsePo getNote(String noteId) {
    GetNoteResponsePo responsePo = new GetNoteResponsePo();
    responsePo.setNote(noteDAO.getNote(noteId));
    responsePo.setTags(tagDAO.getNoteTags(noteId));
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
