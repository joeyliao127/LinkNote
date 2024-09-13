package com.joeyliao.linknoteresource.pojo.coEdit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteContent {

  public NoteContent(String noteContent, String versionId) {
    this.noteContent = noteContent;
    this.versionId = versionId;
  }

  private String noteContent;
  private String versionId;
}
