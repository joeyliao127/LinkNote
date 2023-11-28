package com.linknote.online.linknotespring.note.notedto;


import jakarta.validation.constraints.NotNull;

public class GetNotebooksParamsDto {

  private Integer userId;
  private Integer offset;
  private Integer limit;
  private String keyword;
  private Boolean coNotebook;

  public Boolean getCoNotebook() {
    return coNotebook;
  }

  public void setCoNotebook(Boolean coNotebook) {
    this.coNotebook = coNotebook;
  }

  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
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
}
