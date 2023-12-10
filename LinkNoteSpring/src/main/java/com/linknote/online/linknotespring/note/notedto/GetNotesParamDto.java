package com.linknote.online.linknotespring.note.notedto;

public class GetNotesParamDto {
  private Integer userId;
  private Integer notebookId;
  private Integer offset;
  private Integer limit;
  private String tag;
  private Boolean star;
  private Boolean timeAsc;
  private String keyword;

  private Boolean collaborators;

  public Boolean getCollaborators() {
    return collaborators;
  }

  public void setCollaborators(Boolean collaborators) {
    this.collaborators = collaborators;
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

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public String getTag() {
    return tag;
  }

  public void setTag(String tag) {
    this.tag = tag;
  }

  public Boolean getStar() {
    return star;
  }

  public void setStar(Boolean star) {
    this.star = star;
  }

  public Boolean getTimeAsc() {
    return timeAsc;
  }

  public void setTimeAsc(Boolean timeAsc) {
    this.timeAsc = timeAsc;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }
}
