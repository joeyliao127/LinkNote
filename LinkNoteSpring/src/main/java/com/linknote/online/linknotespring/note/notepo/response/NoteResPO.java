package com.linknote.online.linknotespring.note.notepo.response;

import com.linknote.online.linknotespring.note.notepo.po.NotePO;

public class NoteResPO {
  private Boolean result;
  private NotePO notePO;

  public Boolean getResult() {
    return result;
  }

  public void setResult(Boolean result) {
    this.result = result;
  }

  public NotePO getNotePO() {
    return notePO;
  }

  public void setNotePO(NotePO notePO) {
    this.notePO = notePO;
  }
}
