package com.linknote.online.linknotespring.note.notepo.po;

import java.sql.Timestamp;

public class NotePO {

  private Integer noteId;
  private String name;
  private String question;
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

  public Timestamp getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Timestamp createDate) {
    this.createDate = createDate;
  }

  public Boolean getStar() {
    return star;
  }

  public void setStar(Boolean star) {
    this.star = star;
  }
}
