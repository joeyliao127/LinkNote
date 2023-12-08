package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.TagDao;
import com.linknote.online.linknotespring.note.notedto.GetTagsParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteTagParamDto;
import com.linknote.online.linknotespring.note.noteexception.TagNotFoundException;
import com.linknote.online.linknotespring.note.notepo.po.TagPO;
import com.linknote.online.linknotespring.note.notepo.response.TagResPO;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService{

  @Autowired
  private TagDao tagDao;

  @Autowired
  private IntermediaryService intermediaryService;

  private static final Logger log = LoggerFactory.getLogger(TagService.class);

  @Override
  public TagResPO getTags(GetTagsParamDto params) {
    TagResPO tagResPO = new TagResPO();
    tagResPO.setResult(true);
    if(params.getNoteId() == null){
      tagResPO.setTag(tagDao.getNotebookTags(params));
    }else{
      tagResPO.setTag(tagDao.getNoteTags(params.getNoteId()));
    }
    return tagResPO;
  }

  @Override
  public Integer createNotebookTag(String tag, Integer notebookId) {
    return tagDao.createNotebookTag(tag, notebookId);
  }

  @Override
  public void createNotebookTag(List<String> tagList, Integer notebookId) {
    tagDao.createNotebookTags(tagList, notebookId);
  }


  //update筆記本tag有兩步驟
  //1. 先移除notes_tags種所有noteId的資料
  //2. 新增notes新加入的tag
  @Override
  public void updateNoteTag(UpdateNoteTagParamDto params) {
    List<TagPO> existTag = new ArrayList<>();
    List<TagPO> notExistTag = new ArrayList<>();
    for(TagPO tagPO : params.getTags()){
      if(tagDao.verifyTagExist(params.getNotebookId(), tagPO.getName()) == null){
        notExistTag.add(tagPO);
      }else {
        existTag.add(tagPO);
      }
    }
    params.setTags(existTag);
    tagDao.deleteNoteTags(params.getNoteId());
    tagDao.createNoteTags(params);
    if(!notExistTag.isEmpty()){
      String tagList = "";
      for(TagPO tagPO : notExistTag){
        tagList += tagPO.getName() + ", ";
      }
      throw new TagNotFoundException("在"+ params.getNotebookId()+"筆記本中找不到" + tagList);
    }
  }
}
