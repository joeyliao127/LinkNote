package com.linknote.online.linknotespring.note.notedao;

import com.linknote.online.linknotespring.note.notedto.CreateNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNotebookParamsDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNotebookNameParamDto;
import com.linknote.online.linknotespring.note.notedto.QueryNotebooksParamsDto;
import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import com.linknote.online.linknotespring.note.notepo.po.TagPO;
import java.util.List;

public interface NotebookDao {

  List<NotebooksPO> getNotebooks(QueryNotebooksParamsDto params, Boolean getCoNotebook);

  void createNotebook(CreateNotebookParamsDto params, Integer userId);

  //驗證筆記本是否為此userId
  Integer verifyNotebookOwnerByUserId(Integer userId, Integer notebookId);

  //用途：建立notebook後，取得剛建立notebookId
  Integer getNotebookIdByNotebookName(String notebookName, Integer userId);

  //檢查使用者是否已經有相同名稱的notebook
  String getNotebookNameByUserId(Integer userId, String newNotebook);

  List<TagPO> getNotebookTags(Integer notebookId);

  void updateNotebookName(UpdateNotebookNameParamDto params);

  void updateNoteTags(Integer noteId, Integer tagId);

  void deleteNotebookByNotbookId(DeleteNotebookParamsDto params);


}
