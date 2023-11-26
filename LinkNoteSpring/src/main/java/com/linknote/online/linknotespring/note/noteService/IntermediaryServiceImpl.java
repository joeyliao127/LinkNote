package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.IntermediaryDao;
import com.linknote.online.linknotespring.note.notedto.CreateCollaboratorParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteCollaboraotrsParamDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IntermediaryServiceImpl implements  IntermediaryService{

  @Autowired
  IntermediaryDao intermediaryDao;

  @Override
  public Integer getNoteTagPair(Integer noteId, Integer tagId) {
    return intermediaryDao.getNoteTagPair(noteId, tagId);
  }

  @Override
  public Boolean verifyCollaboratorsCount(Integer ownerId, Integer notebookId) {
    return intermediaryDao.getCollaboratorsCount(ownerId, notebookId) <= 4;
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
  public void deleteCollaborators(DeleteCollaboraotrsParamDto param) {
    intermediaryDao.deleteCollaborators(param);
  }

  @Override
  public void updateNotebookTags(Integer notebookId, Integer tagId) {
    intermediaryDao.updateNotebookTags(notebookId, tagId);
  }

  @Override
  public void createNotTags(Integer tagId, Integer noteId) {
    intermediaryDao.createNotTags(tagId, noteId);
  }
}
