package com.linknote.online.linknotespring.note.notepo.response;

import java.util.List;
import java.util.Map;

public class NotebooksResPO {
  private Boolean result;
  private List<Object> notebooks;
  private List<Object> coNotebooks;
  private Boolean isNotebookNextPage;
  private Boolean isCoNotebookNextPage;

  public static class NotebookPO{
    private String name;
    private int notebookId;
    private boolean selected;
    private String description;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public int getNotebookId() {
      return notebookId;
    }

    public void setNotebookId(int notebookId) {
      this.notebookId = notebookId;
    }

    public boolean isSelected() {
      return selected;
    }

    public void setSelected(boolean selected) {
      this.selected = selected;
    }

    public String getDescription() {
      return description;
    }

    public void setDescription(String description) {
      this.description = description;
    }
  }

  public Boolean getResult() {
    return result;
  }

  public void setResult(Boolean result) {
    this.result = result;
  }

  public List<Object> getNotebooks() {
    return notebooks;
  }

  public void setNotebooks(List<Object> notebooks) {
    this.notebooks = notebooks;
  }

  public List<Object> getCoNotebooks() {
    return coNotebooks;
  }

  public void setCoNotebooks(List<Object> coNotebooks) {
    this.coNotebooks = coNotebooks;
  }

  public Boolean getCoNotebookNextPage() {
    return isCoNotebookNextPage;
  }

  public void setCoNotebookNextPage(Boolean coNotebookNextPage) {
    isCoNotebookNextPage = coNotebookNextPage;
  }

  public Boolean getNotebookNextPage() {
    return isNotebookNextPage;
  }

  public void setNotebookNextPage(Boolean notebookNextPage) {
    isNotebookNextPage = notebookNextPage;
  }
}
