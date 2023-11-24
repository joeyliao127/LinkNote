package com.linknote.online.linknotespring.note.notepo.po;

import java.sql.Date;
import java.util.List;

public class NotePO {

  private String name;
  private String question;
  private Date createTime;
  private Boolean star;

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

  public Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }

  public Boolean getStar() {
    return star;
  }

  public void setStar(Boolean star) {
    this.star = star;
  }
}
