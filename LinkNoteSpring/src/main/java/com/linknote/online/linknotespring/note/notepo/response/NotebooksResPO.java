package com.linknote.online.linknotespring.note.notepo.response;

import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import java.util.List;


public class NotebooksResPO {
  private Boolean result;
  private List<NotebooksPO> notebooks;
  private Boolean nextPage;

  public Boolean getResult() {
    return result;
  }

  public void setResult(Boolean result) {
    this.result = result;
  }

  public List<NotebooksPO> getNotebooks() {
    return notebooks;
  }

  public void setNotebooks(
      List<NotebooksPO> notebooks) {
    this.notebooks = notebooks;
  }

  public Boolean getNextPage() {
    return nextPage;
  }

  public void setNextPage(Boolean nextPage) {
    this.nextPage = nextPage;
  }
}
