package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.IntermediaryDao;
import com.linknote.online.linknotespring.note.notedao.NotebookDao;
import com.linknote.online.linknotespring.note.notedao.TagDao;
import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboraotrsParamDto;
import com.linknote.online.linknotespring.note.notedto.NotebookParamDto;
import com.linknote.online.linknotespring.note.notedto.QueryNotebooksParamsDto;
import com.linknote.online.linknotespring.note.noteexception.NotebookAlreadyExistsException;
import com.linknote.online.linknotespring.note.noteexception.NotebookIdAndUserIdNotMatchException;
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

  @Autowired
  private TagService tagService;

  @Autowired
  private IntermediaryDao intermediaryDao;
  private static final Logger log = LoggerFactory.getLogger(NotebookServiceImpl.class);
  @Override
  public NotebooksResPO getNotebooks(QueryNotebooksParamsDto params) {
    List<NotebooksPO> notebooks = notebookDAO.getNotebooks(params, false);
    log.info("notebook長度：" + notebooks.size());
    List<NotebooksPO> coNotebooks = notebookDAO.getNotebooks(params, true);
    NotebooksResPO response = new NotebooksResPO();

    if(notebooks.size() <= params.getLimit() & !notebooks.isEmpty()){
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
  public void createNotebook(CreateNotebookParamsDto params, Integer userId) {
    String checkNotebookName = notebookDAO.getNotebookNameByUserId(userId, params.getName());
    log.info("查詢到的notebookName，找到代表已經存在:" + checkNotebookName);
    if(checkNotebookName != null){
      throw new NotebookAlreadyExistsException("NotebookService: 名稱已重複");
    }
    notebookDAO.createNotebook(params, userId);
    Integer notebookId = notebookDAO.getNotebookIdByNotebookName(params.getName(), userId);
    log.info("先新增notebook，新增後的id: " + notebookId);
    for(int i=0; i<params.getTags().size(); i++){
      String tag = params.getTags().get(i);
      tagService.createNotebookTag(tag, notebookId, userId);
    }

    List<Integer> collaboratorList = new ArrayList<>();
    for(int i=0; i<params.getEmails().size(); i++){
      String email = params.getEmails().get(i).getEmail();
      int emailId = params.getEmails().get(i).getEmailId();
      log.info("驗證email" + email);
      if(userDAO.verifuUserIdAndEmail(email, emailId) == null){
        log.info("找不到此email ＆ id");
        continue;
      }
      log.info("驗證email和userId通過，插入email ID");
      Integer collaboratorsId = userDAO.getCollaboratorsId(emailId,notebookId);
      if(collaboratorsId == null){
        collaboratorList.add(emailId);
      }
    }
    log.info("email id list完成： " + collaboratorList);
    userDAO.updatyCollaborator(collaboratorList, notebookId);
  }

  @Override
  public void createNotebookTag(String tag, Integer notebookId, Integer userId) {
    tagService.createNotebookTag(tag, notebookId, userId);
  }

  @Override
  public Boolean updateNotebookName(NotebookParamDto params) {

    Integer result = notebookDAO.updateNotebookName(params);
    return result == 1;

  }

  @Override
  public void deleteCollaborators(DeleteCollaboraotrsParamDto params) {
    Integer result = notebookDAO.getNotebookIdByUserId(params.getUserId(), params.getNotebookId());
    if(result == null){
      throw new NotebookIdAndUserIdNotMatchException("Notebook Service: 刪除筆記本失敗，筆記本id部署於此userId");
    }
    intermediaryDao.deleteCollaborators(params);
  }
}
