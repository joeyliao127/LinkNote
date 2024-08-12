package com.joeyliao.linknoteresource.note.dto;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class NoteDTO {
  private String noteId;
  private String name;
  private String question;
  private String content;
  private String keypoint;
  private Timestamp createDate;
  private Boolean star;
}
