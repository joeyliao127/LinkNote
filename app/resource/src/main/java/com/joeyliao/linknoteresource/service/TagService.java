package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.po.tag.CreateNoteTagRequestPo;
import com.joeyliao.linknoteresource.po.tag.CreateNotebookTagRequestPo;
import com.joeyliao.linknoteresource.po.tag.CreateNotebookTagsRequestPo;
import com.joeyliao.linknoteresource.po.tag.DeleteNoteTagRequestPo;
import com.joeyliao.linknoteresource.po.tag.DeleteNotebookTagRequestPo;
import com.joeyliao.linknoteresource.po.tag.GetTagResponsePo;

public interface TagService {

  GetTagResponsePo getNotebookTags(String notebookId);

  GetTagResponsePo getNoteTags(String noteId);

  String createNotebookTag(CreateNotebookTagRequestPo po);

  void createNoteTag(CreateNoteTagRequestPo po);
  
  void deleteNoteTag(DeleteNoteTagRequestPo po);

  void deleteNotebookTag(DeleteNotebookTagRequestPo po);

  void createNotebookTags(CreateNotebookTagsRequestPo tagPo);


}
