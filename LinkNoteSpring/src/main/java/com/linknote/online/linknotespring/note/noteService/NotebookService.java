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
import com.linknote.online.linknotespring.note.notepo.response.NotebooksResPO;
import com.linknote.online.linknotespring.note.notepo.response.NotesResPO;
import com.linknote.online.linknotespring.note.notepo.response.TagResPO;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import java.util.List;

public interface NotebookService {

  NotebooksResPO getNotebooks(GetNotebooksParamsDto params);

  NotesResPO getNotes(GetNotesParamDto params);

  TagResPO getNotebookTags(GetTagsParamDto params);

  List<UserInfoPO> getCollaborators(GetCollaboratorParamDto params);


  Integer createNotebook(CreateNotebookParamsDto params);
  void createNotebookTag(String tag, Integer notebookId, Integer userId);

  Integer createCollaborator(CreateCollaboratorParamsDto params);

  void updateNotebook(UpdateNotebookParamDto params);

  void deleteCollaborators(DeleteCollaboratorsParamDto params);

  void verifyNotebookOwnerByUserId (Integer userId, Integer notebookId, NotebookDao notebookDao );

  void deleteNotebook(DeleteNotebookParamsDto params);

  void deleteNotebookTag(DeleteNotebookTagParamDto param);
}
