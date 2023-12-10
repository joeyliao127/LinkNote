package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NotebookDao;
import com.linknote.online.linknotespring.note.noteexception.PermissionDeniedException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PermissionValidatorServiceImpl implements PermissionValidatorService {

  private static final Logger log = LoggerFactory.getLogger(PermissionValidatorServiceImpl.class);

  @Autowired
  PermissionValidatorDao permissionValidatorDao;
  @Override
  public Boolean verifyNotebookPermission(Integer notebookId, Integer userId) {
    Integer collaboratorId = permissionValidatorDao.verifyNotebookCollaborator(notebookId, userId);
    System.out.println("共編筆記查詢結果：" + collaboratorId);
    Integer ownerId = permissionValidatorDao.verifyNotebookOwner(notebookId, userId);
    System.out.println("Owner查詢結果：" + ownerId);
    if( collaboratorId != null){
      System.out.println("是Collaborator，回傳true");
     return true;
    }else if( ownerId != null){
      System.out.println("不是Collaborator，回傳false");
      return false;
    }
    else{
      log.warn("Permission Service: " + "筆記本id'" + notebookId + "' 不屬於userId' " + userId + "'");
      throw new PermissionDeniedException("userId:" + userId +
          " 嘗試訪問notebookId: " + notebookId + ". 401未授權");
    }


  }
}
