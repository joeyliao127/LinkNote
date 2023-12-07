package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NotebookDao;
import com.linknote.online.linknotespring.note.noteexception.PermissionDeniedException;
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
  public void verifyNotebookPermission(Integer notebookId, Integer userId) {
    Integer id = permissionValidatorDao.verifyNotebookByUserIdAndNotebookId(notebookId, userId);
    if(id == null){
      log.warn("Permission Service: " + "筆記本id'" + notebookId + "' 不屬於userId' " + userId + "'");
      throw new PermissionDeniedException("userId:" + userId +
          " 嘗試訪問notebookId: " + notebookId + ". 401未授權");
    }
  }
}
