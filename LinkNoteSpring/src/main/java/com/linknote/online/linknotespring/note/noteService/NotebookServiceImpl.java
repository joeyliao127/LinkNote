package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NotebookDao;
import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboratorsParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookTagParamDto;
import com.linknote.online.linknotespring.note.notedto.GetCollaboratorParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotesParamDto;
import com.linknote.online.linknotespring.note.notedto.GetTagsParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNotebookParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotebooksParamsDto;
import com.linknote.online.linknotespring.note.noteexception.CollaboratorsAreLimitException;
import com.linknote.online.linknotespring.note.noteexception.NotebookAlreadyExistsException;
import com.linknote.online.linknotespring.note.noteexception.NotebookDoesNotExistException;
import com.linknote.online.linknotespring.note.noteexception.NotebookIdAndUserIdNotMatchException;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.notepo.po.NotesPO;
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;
import com.linknote.online.linknotespring.note.notepo.response.NotesResPO;
import com.linknote.online.linknotespring.note.notepo.response.TagResPO;
import com.linknote.online.linknotespring.user.userdao.UserDAO;
import com.linknote.online.linknotespring.user.userexception.EmailDoesNotExistException;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import com.linknote.online.linknotespring.user.userservice.UserService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

  @Autowired
  PermissionValidatorServiceImpl permissionValidatorService;
  private static final Logger log = LoggerFactory.getLogger(NotebookServiceImpl.class);

  @Override
  public String getUsernameByUserId(Integer userId) {
    return userDAO.getUsernameByUserId(userId);
  }

  @Override
  public NotebooksResPO getNotebooks(GetNotebooksParamsDto params) {
    List<NotebooksPO> notebooks = notebookDao.getNotebooks(params, params.getCoNotebook());
    NotebooksResPO notebooksResPO = new NotebooksResPO();
    if(notebooks.size() <= params.getLimit() & !notebooks.isEmpty()){
      notebooksResPO.setNextPage(false);
    }else if(notebooks.size() > params.getLimit()){
      notebooks.remove(notebooks.size() - 1);
      notebooksResPO.setNextPage(true);
    }else if(notebooks.isEmpty()){
      notebooksResPO.setNextPage(false);
    }
    notebooksResPO.setNotebooks(notebooks);
    notebooksResPO.setResult(true);
    return notebooksResPO;
  }

  @Override
  public NotesResPO getNotes(GetNotesParamDto params) {
    permissionValidatorService.verifyNotebookPermission(params.getNotebookId(), params.getUserId());
    List<NotesPO> notes = notebookDao.getNotes(params);
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
  public TagResPO getNotebookTags(GetTagsParamDto params) {
    permissionValidatorService.verifyNotebookPermission(params.getNotebookId(), params.getUserId());
    return tagService.getTags(params);
  }

  @Override
  public List<UserInfoPO> getCollaborators(GetCollaboratorParamDto params) {
    permissionValidatorService.verifyNotebookPermission(params.getNotebookId(), params.getUserId());
    return notebookDao.getCollaborators(params);
  }

  @Override
  @Transactional
  public Integer createNotebook(CreateNotebookParamsDto params) {
    String checkNotebookName = notebookDao.getNotebookNameByUserId(params.getUserId(), params.getName());
    log.info("查詢到的notebookName，找到代表已經存在: " + checkNotebookName);
    if(checkNotebookName != null){
      throw new NotebookAlreadyExistsException("NotebookService: 名稱已重複");
    }
    notebookDao.createNotebook(params, params.getUserId());
    Integer notebookId = notebookDao.getNotebookIdByNotebookName(params.getName(), params.getUserId());
    tagService.createNotebookTag(params.getTags(), notebookId);
    log.info("開始新增筆記本的協作者，先驗證前端傳來的email和eamil對應的id，確認有此user資訊");
    List<Integer> collaboratorList = new ArrayList<>();
    for(int i=0; i<params.getEmails().size(); i++){
      String email = params.getEmails().get(i).getEmail();
      int emailId = params.getEmails().get(i).getEmailId();
      log.info("驗證email" + email);
      if(userDAO.verifyUserIdAndEmail(email, emailId) == null){
        log.info("找不到此email ＆ id");
        continue;
      }
      log.info("驗證email和userId通過，插入email ID");
      collaboratorList.add(emailId);
    }
   intermediaryService.createNotebookCollaborators(collaboratorList, notebookId, params.getUserId());
    return notebookId;
  }

  @Override
  public Integer createNotebookTag(String tag, Integer notebookId, Integer userId) {
    permissionValidatorService.verifyNotebookPermission(notebookId, userId);
    return tagService.createNotebookTag(tag, notebookId);
  }
  @Override
  public Map<String, Object> createCollaborator(CreateCollaboratorParamsDto params) {
    permissionValidatorService.verifyNotebookPermission(params.getNotebookId(), params.getUserId());
    if(intermediaryService.verifyCollaboratorsCount(params.getUserId(), params.getNotebookId())){
      throw new CollaboratorsAreLimitException("超過共編人數上限");
    }
      Integer collaboratorId = userService.getUserIdByEmail(params.getEmail());
      if(collaboratorId == null){
        throw new EmailDoesNotExistException("此email不存在");
      }
      params.setCollaboratorId(collaboratorId);
      intermediaryService.createNotebookCollaborator(params);
      String username = userDAO.getUsernameByUserId(params.getCollaboratorId());
      Map<String,Object> map = new HashMap<>();
      map.put("userId", collaboratorId);
      map.put("username", username);
      return map;
  }

  @Override
  public void updateNotebook(UpdateNotebookParamDto params) {
    permissionValidatorService.verifyNotebookPermission(params.getNotebookId(), params.getUserId());
    notebookDao.updateNotebook(params);
  }

  @Override
  public void deleteCollaborators(DeleteCollaboratorsParamDto params) {
    permissionValidatorService.verifyNotebookPermission(params.getNotebookId(), params.getUserId());
    intermediaryService.deleteCollaborator(params);
  }

  @Override
  public void deleteNotebook(DeleteNotebookParamsDto params) {
    permissionValidatorService.verifyNotebookPermission(params.getNotebookId(), params.getUserId());
    Integer result = notebookDao.verifyNotebookExist(params.getNotebookId());
    if(result == null){
      throw new NotebookDoesNotExistException("筆記本" + params.getNotebookId() + "不存在");
    }
    notebookDao.deleteNotebookByNotebookId(params);
  }

  @Override
  public void deleteNotebookTag(DeleteNotebookTagParamDto param){
    permissionValidatorService.verifyNotebookPermission(param.getNotebookId(), param.getUserId());
    notebookDao.deleteNotebookTag(param);
  }

}
