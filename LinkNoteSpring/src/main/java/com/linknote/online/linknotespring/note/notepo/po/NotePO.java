package com.linknote.online.linknotespring.note.notepo.po;

import java.sql.Timestamp;

public class NotePO {
  private Integer noteId;
  private String name;
  private String question;
  private String noteContent;
  private String keypoint;
  private Boolean shared;
  private Boolean star;
  private Timestamp createDate;

  public Integer getNoteId() {
    return noteId;
  }

  public void setNoteId(Integer noteId) {
    this.noteId = noteId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getNoteContent() {
    return noteContent;
  }

  public void setNoteContent(String noteContent) {
    this.noteContent = noteContent;
  }

  public String getKeypoint() {
    return keypoint;
  }

  public void setKeypoint(String keypoint) {
    this.keypoint = keypoint;
  }

  public Boolean getShared() {
    return shared;
  }

  public void setShared(Boolean shared) {
    this.shared = shared;
  }

  public Boolean getStar() {
    return star;
  }

  public void setStar(Boolean star) {
    this.star = star;
  }

  public Timestamp getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }
}
