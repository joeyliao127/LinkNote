package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.IntermediaryDao;
import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.CreateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboratorsParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookTagParamDto;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntermediaryServiceImpl implements  IntermediaryService{

  @Autowired
  IntermediaryDao intermediaryDao;


  @Override
  public Boolean verifyCollaboratorsCount(Integer ownerId, Integer notebookId) {
    Integer count = intermediaryDao.getCollaboratorsCount(ownerId, notebookId);
    System.out.println("Count = " + count);
    return count >= 4;
  }

  @Override
  public void createNotebookCollaborators(List<Integer> collaboratorList, Integer notebookId, Integer ownerId) {
    intermediaryDao.createNotebookCollaborators(collaboratorList, notebookId, ownerId);
  }

  @Override
  public void createNotebookCollaborator(CreateCollaboratorParamsDto params) {
    intermediaryDao.createCollaborator(params);
  }

  @Override
  public void deleteCollaborator(DeleteCollaboratorsParamDto param) {
    intermediaryDao.deleteCollaborator(param);
  }

  @Override
  public void deleteCollaborators(DeleteNotebookParamsDto params) {
    intermediaryDao.deleteCollaborators(params);
  }

}
