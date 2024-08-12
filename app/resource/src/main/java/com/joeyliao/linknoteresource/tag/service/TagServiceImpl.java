package com.joeyliao.linknoteresource.tag.service;

import com.joeyliao.linknoteresource.generic.enums.Target;
import com.joeyliao.linknoteresource.generic.uuidgenerator.service.UUIDGeneratorService;
import com.joeyliao.linknoteresource.tag.dao.TagDAO;
import com.joeyliao.linknoteresource.tag.po.CreateNoteTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.CreateNotebookTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.CreateNotebookTagsRequestPo;
import com.joeyliao.linknoteresource.tag.po.DeleteNoteTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.DeleteNotebookTagRequestPo;
import com.joeyliao.linknoteresource.tag.po.GetNoteTagsRequestPo;
import com.joeyliao.linknoteresource.tag.po.GetTagResponsePo;
import com.joeyliao.linknoteresource.tag.po.TagPo;
import com.joeyliao.linknoteresource.tag.po.updateNoteTagRequestPo;
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
