package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NoteDao;
import com.linknote.online.linknotespring.note.notedto.NotebooksQueryParams;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteServiceImpl implements NoteService{

  @Autowired
  private NoteDao noteDAO;

  private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);
  @Override
  public NotebooksResPO getNotebooks(NotebooksQueryParams params) {
    List<NotebooksPO> notebooks = noteDAO.getNotebooks(params, false);
    log.info("notebook長度：" + notebooks.size());
    List<NotebooksPO> coNotebooks = noteDAO.getNotebooks(params, true);
    NotebooksResPO response = new NotebooksResPO();
    
    if(notebooks.size() <= params.getLimit() & !notebooks.isEmpty()){
      log.info("notebook沒有下一頁，list長度：" + notebooks.size());
      response.setNotebookNextPage(false);
      response.setNotebooks(notebooks);
    }else if(notebooks.size() > params.getLimit()){
      notebooks.remove(notebooks.size() - 1);
      response.setNotebookNextPage(true);
      response.setNotebooks(notebooks);
    }else if(notebooks.isEmpty()){
      response.setNotebookNextPage(false);
      response.setNotebooks(notebooks);
    }

    if(coNotebooks.size() <= params.getLimit() & !coNotebooks.isEmpty()){
      response.setCoNotebookNextPage(false);
      response.setCoNotebooks(coNotebooks);
    }else if(coNotebooks.size() > params.getLimit()){
      notebooks.remove(notebooks.size() - 1);
      response.setCoNotebookNextPage(true);
      response.setCoNotebooks(coNotebooks);
    }else if(coNotebooks.isEmpty()){
      response.setCoNotebookNextPage(false);
      response.setCoNotebooks(coNotebooks);
    }
    response.setResult(true);
    return response;
  }
}
