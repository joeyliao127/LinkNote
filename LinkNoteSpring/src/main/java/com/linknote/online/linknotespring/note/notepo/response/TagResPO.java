package com.linknote.online.linknotespring.note.notepo.response;
import com.linknote.online.linknotespring.note.notepo.po.TagPO;
import java.util.List;

public class TagResPO {
  private Boolean result;
  private List<TagPO> tag;

  public List<TagPO> getTag() {
    return tag;
  }

  public void setTag(List<TagPO> tag) {
    this.tag = tag;
  }

  public Boolean getResult() {
    return result;
  }

  public void setResult(Boolean result) {
    this.result = result;
  }

}
