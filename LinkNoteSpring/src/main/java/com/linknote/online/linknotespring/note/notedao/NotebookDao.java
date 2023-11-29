package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookTagParamDto;
import com.linknote.online.linknotespring.note.notedto.GetCollaboratorParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotesParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNotebookParamDto;
import com.linknote.online.linknotespring.note.notedto.GetNotebooksParamsDto;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.notepo.po.NotesPO;
import com.linknote.online.linknotespring.note.notepo.po.TagPO;
import com.linknote.online.linknotespring.user.userpo.UserInfoPO;
import java.util.List;

public interface NotebookDao {

  List<NotebooksPO> getNotebooks(GetNotebooksParamsDto params, Boolean getCoNotebook);

  List<NotesPO> getNotes(GetNotesParamDto params);

  List<UserInfoPO> getCollaborators(GetCollaboratorParamDto param);

  void createNotebook(CreateNotebookParamsDto params, Integer userId);

  Integer verifyNotebookExist(Integer notebookId);
  //驗證筆記本是否為此userId
  Integer verifyNotebookOwnerByUserId(Integer userId, Integer notebookId);

  //用途：建立notebook後，取得剛建立notebookId
  Integer getNotebookIdByNotebookName(String notebookName, Integer userId);

  //檢查使用者是否已經有相同名稱的notebook
  String getNotebookNameByUserId(Integer userId, String newNotebook);

  List<TagPO> getNotebookTags(Integer notebookId);

  void updateNotebook(UpdateNotebookParamDto params);

  void updateNoteTags(Integer noteId, Integer tagId);

  void deleteNotebookByNotebookId(DeleteNotebookParamsDto params);

  void deleteNotebookTag(DeleteNotebookTagParamDto param);



}
