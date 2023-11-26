package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.IntermediaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntermediaryServiceImpl implements  IntermediaryService{

  @Autowired
  IntermediaryDao intermediaryDao;

  @Override
  public void createNotebookCollaborator(Integer notebookId, Integer collaboratorId) {
    String sql = "INSERT INTO notebookCollaborators (userId, notebookId) VALUES (:userId, :notebookId)";
    
  }
}
