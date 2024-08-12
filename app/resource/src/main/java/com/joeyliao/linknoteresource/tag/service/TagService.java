package com.joeyliao.linknoteresource.tag.service;

import com.joeyliao.linknoteresource.tag.po.CreateNoteTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.CreateNotebookTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.CreateNotebookTagsRequestPo;
import com.joeyliao.linknoteresource.tag.po.DeleteNoteTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.DeleteNotebookTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.GetNoteTagsRequestPo;
import com.joeyliao.linknoteresource.tag.po.GetTagResponsePo;
import com.joeyliao.linknoteresource.tag.po.updateNoteTagRequestPo;

public interface TagService {

  GetTagResponsePo getNotebookTags(String notebookId);

  GetTagResponsePo getNoteTags(String noteId);

  String createNotebookTag(CreateNotebookTagRequestPo po);

  void createNoteTag(CreateNoteTagRequestPo po);
  
  void deleteNoteTag(DeleteNoteTagRequestPo po);

  void deleteNotebookTag(DeleteNotebookTagRequestPo po);

  void createNotebookTags(CreateNotebookTagsRequestPo tagPo);


}
