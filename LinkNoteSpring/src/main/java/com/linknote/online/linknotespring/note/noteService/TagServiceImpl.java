package com.linknote.online.linknotespring.note.noteService;

import com.linknote.online.linknotespring.note.notedao.TagDao;
import com.linknote.online.linknotespring.note.notedto.CreateNoteTagParamDto;
import com.linknote.online.linknotespring.note.notedto.DeleteNoteParamDto;
import com.linknote.online.linknotespring.note.noteexception.TagNotFoundException;
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
  public void createNotebookTag(String tag, Integer notebookId) {
    tagDao.createNotebookTag(tag, notebookId);
  }

  @Override
  public void createNotebookTag(List<String> tagList, Integer notebookId) {
    tagDao.createNotebookTags(tagList, notebookId);
  }


  @Override
  public void createNoteTag(CreateNoteTagParamDto params) {
    if(tagDao.verifyTagExist(params.getNotebookId(), params.getTag()) == null){
      throw new TagNotFoundException("在"+ params.getNotebookId()+"筆記本中找不到" + params.getTag());
    }
    tagDao.createNoteTag(params);
  }

  @Override
  public void deleteNoteTag(DeleteNoteParamDto param) {
    tagDao.deleteNoteTag(param);
  }
}
