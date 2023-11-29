package com.linknote.online.linknotespring.note.notepo.response;

import java.util.List;

public class TagResPO {
  private Boolean result;
  private List<String> tag;

  public Boolean getResult() {
    return result;
  }

  public void setResult(Boolean result) {
    this.result = result;
  }

  public List<String> getTag() {
    return tag;
  }

  public void setTag(List<String> tag) {
    this.tag = tag;
  }
}
