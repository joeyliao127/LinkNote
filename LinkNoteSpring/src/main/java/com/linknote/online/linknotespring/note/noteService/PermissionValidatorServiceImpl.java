package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NotebookDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PermissionValidatorServiceImpl implements PermissionValidatorService {

  private static final Logger log = LoggerFactory.getLogger(PermissionValidatorServiceImpl.class);

  @Autowired
  NotebookDao notebookDao;
  @Override
  public Boolean verifyNotebookPerission(Integer notebookId, Integer userId) {

    return null;
  }
}
