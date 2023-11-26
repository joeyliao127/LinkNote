package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.NotebookDao;
import com.linknote.online.linknotespring.note.notedao.TagDao;
import com.linknote.online.linknotespring.note.notedao.IntermediaryDao;
import com.linknote.online.linknotespring.note.notedto.CreateTagParamDto;
import com.linknote.online.linknotespring.note.noteexception.NotebookIdAndUserIdNotMatchException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService{

  @Autowired
  private TagDao tagDao;

  @Autowired
  private IntermediaryDao intermediaryDao;

  @Autowired
 private IntermediaryService intermediaryService;

  private static final Logger log = LoggerFactory.getLogger(TagService.class);

  //createTag有自動檢查是否有重複的tag，並且插入notebookTag中介表關聯資訊
  @Override
  public void createNotebookTag(String tag, Integer notebookId, Integer userId) {
    Integer tagId = tagDao.getTagIdByTagName(tag);
    log.info("是否有此tag: " + tag);
    if(tagId == null){
      log.info("沒有，建立新tag");
      tagDao.createTag(tag);
      tagId = tagDao.getTagIdByTagName(tag);
      intermediaryService.updateNotebookTags(notebookId, tagId);
    }else{
      log.info("有");
      intermediaryService.updateNotebookTags(notebookId, tagId);
    }
  }

  @Override
  public void createNoteTag(CreateTagParamDto params) {
    Integer result = intermediaryService.getNoteTagPair(params.getTagId(), params.getNoteId());
    if(result == null){
     intermediaryService.createNotTags(params.getTagId(), params.getNoteId());
    }
  }

}
