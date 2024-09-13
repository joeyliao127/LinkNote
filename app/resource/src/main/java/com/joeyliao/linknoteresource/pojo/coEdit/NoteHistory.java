package com.joeyliao.linknoteresource.pojo.coEdit;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteHistory {

  public NoteHistory(String latestVersionId, ArrayList<String> noteContent) {
    this.latestVersionId = latestVersionId;
    this.noteContent = noteContent;
    this.editHistory = new ConcurrentHashMap<>();
  }

  private String latestVersionId;
  private ArrayList<String> noteContent;
  private ConcurrentHashMap<String, EditPositionVersion> editHistory;

  //  {
//    "latestNoteContent": "test",
//    "latestVersionId": "versionId",
//    "editHistory": {
//      "versionId": {
//        editPosition: [1, 2],
//        nextInsertPosition: [1, 2]
//    }
//  }
}

