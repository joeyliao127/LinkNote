package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NotebookDao;
import com.linknote.online.linknotespring.note.notedao.TagDao;
import com.linknote.online.linknotespring.note.notedao.TagDaoImpl;
import com.linknote.online.linknotespring.note.notedto.NotebookCreateParamsDTO;
import com.linknote.online.linknotespring.note.notedto.NotebooksQueryParamsDTO;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;
import com.linknote.online.linknotespring.user.userdao.UserDAO;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotebookServiceImpl implements NotebookService {

  @Autowired
  private NotebookDao notebookDAO;
  @Autowired
  private TagDao tagDao;

  @Autowired
  private UserDAO userDAO;

  private static final Logger log = LoggerFactory.getLogger(NotebookServiceImpl.class);
  @Override
  public NotebooksResPO getNotebooks(NotebooksQueryParamsDTO params) {
    List<NotebooksPO> notebooks = notebookDAO.getNotebooks(params, false);
    log.info("notebook長度：" + notebooks.size());
    List<NotebooksPO> coNotebooks = notebookDAO.getNotebooks(params, true);
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
    notebookDAO.createNotebook(params, userId);
    log.info("先新增notebook");
    Integer notebookId = notebookDAO.getNotebookIdByNotebookName(params.getName());
    log.info("開始新增tag，首先檢查tag，tag為：");
    for(int i=0; i<params.getTags().size(); i++){
      String tag = params.getTags().get(i);
      Integer tagId = tagDao.getTagIdByTagName(tag);
      log.info("是否有此tag: " + tag);
      if(tagId == null){
        log.info("沒有，建立新tag");
        tagDao.createTag(tag);
        tagId = tagDao.getTagIdByTagName(tag);
        notebookDAO.updateNotebookTags(notebookId, tagId);
      }else{
        log.info("有");
        notebookDAO.updateNotebookTags(notebookId, tagId);
      }
    }

    List<Integer> collaboratorList = new ArrayList<>();
    for(int i=0; i<params.getEmails().size(); i++){
      String email = params.getEmails().get(i).getEmail();
      if(userDAO.getUserIdByEmail(email) == null){
        continue;
      }
      int emailId = params.getEmails().get(i).getEmailId();
      Integer collaboratorsId = userDAO.getCollaboratorsId(emailId,notebookId);
      if(collaboratorsId == null){
        collaboratorList.add(emailId);
      }
    }
    log.info("email id list完成： " + collaboratorList);
    userDAO.updatyCollaborator(collaboratorList, notebookId);
  }
}
