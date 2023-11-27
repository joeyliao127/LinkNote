package com.linknote.online.linknotespring.note.notedto;

public class UpdateNoteStarParamDto {

  private Integer notebookId;
  private Integer noteId;
  private Integer userId;

  private boolean star;

  public Integer getNotebookId() {
    return notebookId;
  }

  public void setNotebookId(Integer notebookId) {
    this.notebookId = notebookId;
  }

  public Integer getNoteId() {
    return noteId;
  }

  public void setNoteId(Integer noteId) {
    this.noteId = noteId;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public boolean isStar() {
    return star;
  }

  public void setStar(boolean star) {
    this.star = star;
  }
}
