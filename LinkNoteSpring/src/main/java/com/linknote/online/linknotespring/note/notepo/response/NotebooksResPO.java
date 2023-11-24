package com.linknote.online.linknotespring.note.notepo.response;

import com.linknote.online.linknotespring.note.notepo.po.NotebooksPO;
import java.util.List;
import java.util.Map;

public class NotebooksResPO {

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

  public List<NotebooksPO> getCoNotebooks() {
    return coNotebooks;
  }

  public void setCoNotebooks(
      List<NotebooksPO> coNotebooks) {
    this.coNotebooks = coNotebooks;
  }

  public Boolean getNotebookNextPage() {
    return isNotebookNextPage;
  }

  public void setNotebookNextPage(Boolean notebookNextPage) {
    isNotebookNextPage = notebookNextPage;
  }

  public Boolean getCoNotebookNextPage() {
    return isCoNotebookNextPage;
  }

  public void setCoNotebookNextPage(Boolean coNotebookNextPage) {
    isCoNotebookNextPage = coNotebookNextPage;
  }

  private Boolean result;
  private List<NotebooksPO> notebooks;
  private List<NotebooksPO> coNotebooks;
  private Boolean isNotebookNextPage;
  private Boolean isCoNotebookNextPage;


}
