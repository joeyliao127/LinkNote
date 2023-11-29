package com.linknote.online.linknotespring.note.notepo.response;

import com.linknote.online.linknotespring.note.notepo.po.NotePO;
import java.util.List;

public class NoteResPO {
  private Boolean result;
  private List<NotePO> notes;
  private Boolean nextPage;

  public Boolean getResult() {
    return result;
  }

  public void setResult(Boolean result) {
    this.result = result;
  }

  public List<NotePO> getNotes() {
    return notes;
  }

  public void setNotes(List<NotePO> notes) {
    this.notes = notes;
  }

  public Boolean getNextPage() {
    return nextPage;
  }

  public void setNextPage(Boolean nextPage) {
    this.nextPage = nextPage;
  }
}
