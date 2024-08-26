package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.enums.generic.Target;
import com.joeyliao.linknoteresource.dao.TagDAO;
import com.joeyliao.linknoteresource.po.tag.CreateNoteTagRequestPo;
import com.joeyliao.linknoteresource.po.tag.CreateNotebookTagRequestPo;
import com.joeyliao.linknoteresource.po.tag.CreateNotebookTagsRequestPo;
import com.joeyliao.linknoteresource.po.tag.DeleteNoteTagRequestPo;
import com.joeyliao.linknoteresource.po.tag.DeleteNotebookTagRequestPo;
import com.joeyliao.linknoteresource.po.tag.GetTagResponsePo;
import com.joeyliao.linknoteresource.po.tag.TagPo;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

  @Autowired
  TagDAO tagDAO;

  @Autowired
  UUIDGeneratorService uuidGeneratorService;
  @Override
  public GetTagResponsePo getNotebookTags(String notebookId) {
    GetTagResponsePo po = new GetTagResponsePo();
    po.setTags(tagDAO.getNotebookTags(notebookId));
    return po;
  }

  @Override
  public GetTagResponsePo getNoteTags(String noteId) {
    GetTagResponsePo po = new GetTagResponsePo();
    po.setTags(tagDAO.getNoteTags(noteId));
    return po;
  }

  @Override
  public String createNotebookTag(CreateNotebookTagRequestPo po) {
    String tagId = uuidGeneratorService.generateUUID(Target.TAG);
    po.setTagId(tagId);
    tagDAO.createNotebookTag(po);
    return tagId;
  }

  @Override
  public void createNotebookTags(CreateNotebookTagsRequestPo po) {
    List<TagPo> tags = po.getTags();
    for (TagPo tag : tags) {
      tag.setTagId(uuidGeneratorService.generateUUID(Target.TAG));
    }
    tagDAO.createNotebookTags(po);
  }


  @Override
  public void createNoteTag(CreateNoteTagRequestPo po) {
    tagDAO.createNoteTag(po);
  }

  @Override
  public void deleteNoteTag(DeleteNoteTagRequestPo po) {
    tagDAO.deleteNoteTag(po);
  }

  @Override
  public void deleteNotebookTag(DeleteNotebookTagRequestPo po) {
    tagDAO.deleteNotebookTag(po);
  }


}
