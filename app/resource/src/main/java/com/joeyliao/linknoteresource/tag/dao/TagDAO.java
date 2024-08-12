package com.joeyliao.linknoteresource.tag.dao;

import com.joeyliao.linknoteresource.tag.dto.TagDTO;
import com.joeyliao.linknoteresource.tag.po.CreateNoteTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.CreateNotebookTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.CreateNotebookTagsRequestPo;
import com.joeyliao.linknoteresource.tag.po.DeleteNoteTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.DeleteNotebookTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.GetNoteTagsRequestPo;
import com.joeyliao.linknoteresource.tag.po.GetTagResponsePo;
import com.joeyliao.linknoteresource.tag.po.updateNoteTagRequestPo;
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
