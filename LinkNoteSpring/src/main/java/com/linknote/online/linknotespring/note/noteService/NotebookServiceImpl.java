package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NotebookDao;
import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboraotrsParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNotebookNameParamDto;
import com.linknote.online.linknotespring.note.notedto.QueryNotebooksParamsDto;
import com.linknote.online.linknotespring.note.noteexception.CollaboratorsAreLimitException;
import com.linknote.online.linknotespring.note.noteexception.NotebookAlreadyExistsException;
import com.linknote.online.linknotespring.note.noteexception.NotebookIdAndUserIdNotMatchException;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;
import com.linknote.online.linknotespring.user.userdao.UserDAO;
import com.linknote.online.linknotespring.user.userexception.EmailDoesNotExistException;
import com.linknote.online.linknotespring.user.userservice.UserService;
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
  private NotebookDao notebookDao;

  @Autowired
  private UserDAO userDAO;

  @Autowired
  private TagService tagService;

  @Autowired
  private IntermediaryService intermediaryService;

  @Autowired
  private UserService userService;
  private static final Logger log = LoggerFactory.getLogger(NotebookServiceImpl.class);
  @Override
  public NotebooksResPO getNotebooks(QueryNotebooksParamsDto params) {
    List<NotebooksPO> notebooks = notebookDao.getNotebooks(params, false);
    log.info("notebook長度：" + notebooks.size());
    List<NotebooksPO> coNotebooks = notebookDao.getNotebooks(params, true);
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
    String checkNotebookName = notebookDao.getNotebookNameByUserId(userId, params.getName());
    log.info("查詢到的notebookName，找到代表已經存在: " + checkNotebookName);
    if(checkNotebookName != null){
      throw new NotebookAlreadyExistsException("NotebookService: 名稱已重複");
    }
    notebookDao.createNotebook(params, userId);
    Integer notebookId = notebookDao.getNotebookIdByNotebookName(params.getName(), userId);
    log.info("先新增notebook，新增後的id: " + notebookId);
    for(int i=0; i<params.getTags().size(); i++){
      String tag = params.getTags().get(i);
      tagService.createNotebookTag(tag, notebookId, userId);
    }

    log.info("開始新增筆記本的協作者，先驗證前端傳來的email和eamil對應的id，確認有此user資訊");
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
      collaboratorList.add(emailId);
    }
    log.info("email id list完成： " + collaboratorList);
   intermediaryService.createNotebookCollaborators(collaboratorList, notebookId, userId);
  }

  @Override
  public void createNotebookTag(String tag, Integer notebookId, Integer userId) {
    Boolean res = verifyNotebookOwnerByUserId(userId, notebookId, notebookDao);
    if(res){
      tagService.createNotebookTag(tag, notebookId, userId);
    }
  }
  @Override
  public void createCollaborator(CreateCollaboratorParamsDto params) {
    Boolean res = verifyNotebookOwnerByUserId(params.getUserId(), params.getNotebookId(), notebookDao);
    if(intermediaryService.verifyCollaboratorsCount(params.getUserId(), params.getCollaboratorId())){
      throw new CollaboratorsAreLimitException("超過共編人數上限");
    }
    if(res){
      Integer collaboratorId = userService.getUserIdByEmail(params.getEmail());
      if(collaboratorId == null){
        throw new EmailDoesNotExistException("此email不存在");
      }
      params.setCollaboratorId(collaboratorId);
      intermediaryService.createNotebookCollaborator(params);
    }
  }

  @Override
  public void updateNotebookName(UpdateNotebookNameParamDto params) {
    Boolean res = verifyNotebookOwnerByUserId(params.getUserId(), params.getNotebookId(), notebookDao);
    if(res){
      notebookDao.updateNotebookName(params);
    }
  }

  @Override
  public void deleteCollaborators(DeleteCollaboraotrsParamDto params) {
    Boolean res = verifyNotebookOwnerByUserId(params.getUserId(), params.getNotebookId(), notebookDao);
    if(res){
      intermediaryService.deleteCollaborators(params);
    }
  }


  //只有insert的service才需要驗證筆記本的owner
  public static Boolean verifyNotebookOwnerByUserId(Integer userId, Integer notebookId, NotebookDao notebookDao ){
    Integer result = notebookDao.verifyNotebookOwnerByUserId(userId, notebookId);
    if(result == null){
      throw new NotebookIdAndUserIdNotMatchException("Notebook Service: 筆記本不署於此userId");
    }
    return true;
  }
}
