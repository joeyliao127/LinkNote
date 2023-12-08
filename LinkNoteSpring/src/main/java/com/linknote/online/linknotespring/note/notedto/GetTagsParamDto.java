package com.linknote.online.linknotespring.note.notedto;

public class GetTagsParamDto {

  private Integer notebookId;
  private Integer noteId;
  private Integer userId;

  private Boolean collaborators;

  public Boolean getCollaborators() {
    return collaborators;
  }

  public void setCollaborators(Boolean collaborators) {
    this.collaborators = collaborators;
  }

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
}
