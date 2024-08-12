package com.joeyliao.linknoteresource.notebook.dao;

import com.joeyliao.linknoteresource.collaborator.po.NotebookOwnerDTO;
import com.joeyliao.linknoteresource.notebook.dto.NotebooksDTO;
import com.joeyliao.linknoteresource.notebook.po.GetNotebooksRequestPo;
import com.joeyliao.linknoteresource.notebook.po.CreateNotebookRequestPo;
import com.joeyliao.linknoteresource.notebook.po.UpdateNotebookPo;
import java.util.List;

public interface NotebookDAO {
  void createNotebook(CreateNotebookRequestPo po, String id);

  List<NotebooksDTO> getNotebooks(GetNotebooksRequestPo po);

  List<NotebooksDTO> getCoNotebooks(GetNotebooksRequestPo po);

  Integer updateNotebook(UpdateNotebookPo po);

  Integer deleteNotebook(String notebookId);

  NotebookOwnerDTO getNotebookOwner(String notebookId);

  String getNotebookName(String notebookId);
}
