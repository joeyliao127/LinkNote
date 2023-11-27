package com.linknote.online.linknotespring.note.notedto;

public class DeleteNoteParamDto {
  private Integer userId;
  private Integer noteId;
  private Integer notebookId;

  private String tagName;

  public String getTagName() {
    return tagName;
  }

  public void setTagName(String tagName) {
    this.tagName = tagName;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public Integer getNoteId() {
    return noteId;
  }

  public void setNoteId(Integer noteId) {
    this.noteId = noteId;
  }

  public Integer getNotebookId() {
    return notebookId;
  }

  public void setNotebookId(Integer notebookId) {
    this.notebookId = notebookId;
  }
}
