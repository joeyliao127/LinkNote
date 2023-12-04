package com.linknote.online.linknotespring.note.notedto;

import com.linknote.online.linknotespring.note.notepo.po.TagPO;
import java.util.List;

public class UpdateNoteTagParamDto {
  private List<TagPO> tags;
  private Integer userId;
  private Integer notebookId;
  private Integer noteId;

  public Integer getNoteId() {
    return noteId;
  }

  public void setNoteId(Integer noteId) {
    this.noteId = noteId;
  }


  public List<TagPO> getTags() {
    return tags;
  }

  public void setTags(List<TagPO> tags) {
    this.tags = tags;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getNotebookId() {
    return notebookId;
  }

  public void setNotebookId(Integer notebookId) {
    this.notebookId = notebookId;
  }
}
