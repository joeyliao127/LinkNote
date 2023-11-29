package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.TagDao;
import com.linknote.online.linknotespring.note.notedto.GetTagsParamDto;
import com.linknote.online.linknotespring.note.notedto.UpdateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.noteexception.TagNotFoundException;
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
      tagResPO.setTag(tagDao.getTags(params.getUserId(), params.getNotebookId()));
    }else{
      tagResPO.setTag(tagDao.getTags(params.getUserId(), params.getNotebookId(), params.getNoteId()));
    }
    return tagResPO;
  }

  @Override
  public void createNotebookTag(String tag, Integer notebookId) {
    tagDao.createNotebookTag(tag, notebookId);
  }

  @Override
  public void createNotebookTag(List<String> tagList, Integer notebookId) {
    tagDao.createNotebookTags(tagList, notebookId);
  }


  @Override
  public void createNoteTag(UpdateNoteTagParamDto params) {
    List<String> existTag = new ArrayList<>();
    List<String> notExistTag = new ArrayList<>();
    for(String tag : params.getTag()){
      if(tagDao.verifyTagExist(params.getNotebookId(), tag) == null){
        notExistTag.add(tag);
      }else {
        existTag.add(tag);
      }
    }
    params.setTag(existTag);
    tagDao.createNoteTag(params);
    if(!notExistTag.isEmpty()){
      String tagList = "";
      for(String tag : notExistTag){
        tagList += tag + ", ";
      }
      throw new TagNotFoundException("在"+ params.getNotebookId()+"筆記本中找不到" + tagList);
    }
  }

  @Override
  public void deleteNoteTag(DeleteNoteParamDto param) {
    tagDao.deleteNoteTag(param);
  }
}
