package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.dto.notebookdto.NotebooksDTO;
import com.joeyliao.linknoteresource.po.notebookpo.GetNotebooksRequestPo;
import com.joeyliao.linknoteresource.po.notebookpo.GetNotebooksResponsePo;
import com.joeyliao.linknoteresource.po.notebookpo.CreateNotebookRequestPo;
import com.joeyliao.linknoteresource.po.notebookpo.UpdateNotebookPo;

public interface NotebookService {

  String createNotebook(CreateNotebookRequestPo po, String authorization);

  GetNotebooksResponsePo getNotebooks(GetNotebooksRequestPo po);

  GetNotebooksResponsePo getCoNotebooks(GetNotebooksRequestPo po);

  NotebooksDTO getNotebook(String notebookId);

  void updateNotebook(UpdateNotebookPo po);

  void deleteNotebook(String notebookId);
}
