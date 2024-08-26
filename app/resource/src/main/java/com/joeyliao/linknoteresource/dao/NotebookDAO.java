package com.joeyliao.linknoteresource.dao;

import com.joeyliao.linknoteresource.po.collaboratorpo.NotebookOwnerDTO;
import com.joeyliao.linknoteresource.dto.notebookdto.NotebooksDTO;
import com.joeyliao.linknoteresource.po.notebookpo.GetNotebooksRequestPo;
import com.joeyliao.linknoteresource.po.notebookpo.CreateNotebookRequestPo;
import com.joeyliao.linknoteresource.po.notebookpo.UpdateNotebookPo;
import java.util.List;

public interface NotebookDAO {
  void createNotebook(CreateNotebookRequestPo po, String id);

  List<NotebooksDTO> getNotebooks(GetNotebooksRequestPo po);

  List<NotebooksDTO> getCoNotebooks(GetNotebooksRequestPo po);

  Integer updateNotebook(UpdateNotebookPo po);

  Integer deleteNotebook(String notebookId);

  NotebookOwnerDTO getNotebookOwner(String notebookId);

  NotebooksDTO getNotebook(String notebookId);
}
