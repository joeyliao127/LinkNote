package com.joeyliao.linknoteresource.notebook.service;

import com.joeyliao.linknoteresource.notebook.po.GetNotebooksRequestPo;
import com.joeyliao.linknoteresource.notebook.po.GetNotebooksResponsePo;
import com.joeyliao.linknoteresource.notebook.po.CreateNotebookRequestPo;
import com.joeyliao.linknoteresource.notebook.po.UpdateNotebookPo;

public interface NotebookService {

  String createNotebook(CreateNotebookRequestPo po, String authorization);

  GetNotebooksResponsePo getNotebooks(GetNotebooksRequestPo po);

  GetNotebooksResponsePo getCoNotebooks(GetNotebooksRequestPo po);

  void updateNotebook(UpdateNotebookPo po);

  void deleteNotebook(String notebookId);
}
