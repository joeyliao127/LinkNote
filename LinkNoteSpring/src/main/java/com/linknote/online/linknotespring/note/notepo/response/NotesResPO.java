package com.linknote.online.linknotespring.note.notepo.response;

import com.linknote.online.linknotespring.note.notepo.po.NotesPO;
import java.util.List;

public class NotesResPO {
  private Boolean result;
  private List<NotesPO> notes;
  private Boolean nextPage;

  public Boolean getResult() {
    return result;
  }

  public void setResult(Boolean result) {
    this.result = result;
  }

  public List<NotesPO> getNotes() {
    return notes;
  }

  public void setNotes(List<NotesPO> notes) {
    this.notes = notes;
  }

  public Boolean getNextPage() {
    return nextPage;
  }

  public void setNextPage(Boolean nextPage) {
    this.nextPage = nextPage;
  }
}
