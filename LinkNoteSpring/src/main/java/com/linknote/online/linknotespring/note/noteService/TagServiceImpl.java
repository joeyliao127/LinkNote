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
  private NotebookDao notebookDao;

  private static final Logger log = LoggerFactory.getLogger(TagService.class);

  //先檢查notebookId是否屬於此使用者
  //createTag有自動檢查是否有重複的tag，並且插入notebookTag中介表關聯資訊
  @Override
  public Boolean createNotebookTag(String tag, Integer notebookId, Integer userId) {
    Integer result = notebookDao.getNotebookIdByUserId(userId, notebookId);
    log.info("userId:" + userId);
    log.info("result id: " + result);
    log.info("notebook id:" + notebookId);
    if(!Objects.equals(result, notebookId)){
      throw new NotebookIdAndUserIdNotMatchException("TagService: notebookID and user id not match");
    }
    Integer tagId = tagDao.getTagIdByTagName(tag);
    log.info("是否有此tag: " + tag);
    if(tagId == null){
      log.info("沒有，建立新tag");
      tagDao.createTag(tag);
      tagId = tagDao.getTagIdByTagName(tag);
      Integer impactRow = intermediaryDao.updateNotebookTags(notebookId, tagId);
      return impactRow != 0;
    }else{
      log.info("有");
      Integer impactRow = intermediaryDao.updateNotebookTags(notebookId, tagId);
      return impactRow != 0;
    }
  }

  @Override
  public Boolean createNoteTag(CreateTagParamDto params) {
    Integer result = intermediaryDao.getNoteTagPair(params.getTagId(), params.getNoteId());
    if(result == null){
     Integer impactRow = intermediaryDao.createNotTags(params.getTagId(), params.getNoteId());
     return impactRow == 1;
    }
    return false;
  }

}
