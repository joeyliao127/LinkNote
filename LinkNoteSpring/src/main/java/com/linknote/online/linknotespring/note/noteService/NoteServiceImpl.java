package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NoteDao;
import com.linknote.online.linknotespring.note.notedto.NotebookCreateParamsDTO;
import com.linknote.online.linknotespring.note.notedto.NotebooksQueryParamsDTO;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;
import com.linknote.online.linknotespring.user.userdao.UserDAO;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteServiceImpl implements NoteService{

  @Autowired
  private NoteDao noteDAO;
  @Autowired
  private UserDAO userDAO;

  private static final Logger log = LoggerFactory.getLogger(NoteServiceImpl.class);
  @Override
  public NotebooksResPO getNotebooks(NotebooksQueryParamsDTO params) {
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

  @Override
  @Transactional
  public void createNotebook(NotebookCreateParamsDTO params, Integer userId) {
    noteDAO.createNotebook(params, userId);
    log.info("先新增notebook");
    Integer notebookId = noteDAO.getNotebookIdByNotebookName(params.getName());
    log.info("開始新增tag，首先檢查tag，tag為：");
    log.info(params.getTags().get(0));
    log.info(params.getTags().get(1));
    log.info(params.getTags().get(2));
    log.info(String.valueOf(params.getTags().size()));
    for(int i=0; i<params.getTags().size(); i++){
      log.info("進入迴圈");
      log.info("i = " + i);
      String tag = params.getTags().get(i);
      log.info("tag = " + tag);
      Integer tagId = noteDAO.getTagIdByTagName(tag);
      log.info("tag id = " + tagId);
      log.info("是否有此tag: " + tag);
      if(tagId == null){
        log.info("沒有，建立新tag");
        noteDAO.createTag(tag);
        tagId = noteDAO.getTagIdByTagName(tag);
        noteDAO.updateNotebookTags(notebookId, tagId);
      }else{
        log.info("有");
        noteDAO.updateNotebookTags(notebookId, tagId);
      }
    }

  }
}
