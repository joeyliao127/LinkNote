package com.joeyliao.linknoteresource.dao;

import com.joeyliao.linknoteresource.dto.tag.TagDTO;
import com.joeyliao.linknoteresource.po.tag.CreateNoteTagRequestPo;
import com.joeyliao.linknoteresource.po.tag.CreateNotebookTagRequestPo;
import com.joeyliao.linknoteresource.po.tag.CreateNotebookTagsRequestPo;
import com.joeyliao.linknoteresource.po.tag.DeleteNoteTagRequestPo;
import com.joeyliao.linknoteresource.po.tag.DeleteNotebookTagRequestPo;
import java.util.List;

public interface TagDAO {

  List<TagDTO> getNotebookTags(String notebookId);

  List<TagDTO> getNoteTags(String noteId);

  void createNotebookTag(CreateNotebookTagRequestPo po);

  void createNoteTag(CreateNoteTagRequestPo po);

  void deleteNoteTag(DeleteNoteTagRequestPo po);

  void deleteNotebookTag(DeleteNotebookTagRequestPo po);


  void createNotebookTags(CreateNotebookTagsRequestPo tagPo);


}
