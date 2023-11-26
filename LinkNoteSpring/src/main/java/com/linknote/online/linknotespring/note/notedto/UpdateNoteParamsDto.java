package com.linknote.online.linknotespring.note.notedto;

import io.swagger.v3.oas.models.security.SecurityScheme.In;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class UpdateNoteParamsDto {
  private Integer notebookId;
  private Integer noteId;
  private Integer userId;

  private String name;

  private String question;
  private String content;
  private String keypoint;
  private Boolean sharedPermission;
  private Boolean star;
  List<Integer> tags;

  public List<Integer> getTags() {
    return tags;
  }

  public void setTags(List<Integer> tags) {
    this.tags = tags;
  }

  public Boolean getSharedPermission() {
    return sharedPermission;
  }

  public void setSharedPermission(Boolean sharedPermission) {
    this.sharedPermission = sharedPermission;
  }

  public Boolean getStar() {
    return star;
  }

  public void setStar(Boolean star) {
    this.star = star;
  }


  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }
  public Integer getNoteId() {
    return noteId;
  }

  public void setNoteId(Integer noteId) {
    this.noteId = noteId;
  }

  public Integer getNotebookId() {
    return notebookId;
  }

  public void setNotebookId(Integer notebookId) {
    this.notebookId = notebookId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getKeypoint() {
    return keypoint;
  }

  public void setKeypoint(String keypoint) {
    this.keypoint = keypoint;
  }

  public boolean isSharedPermission() {
    return sharedPermission;
  }

  public void setSharedPermission(boolean sharedPermission) {
    this.sharedPermission = sharedPermission;
  }

  public boolean isStar() {
    return star;
  }

  public void setStar(boolean star) {
    this.star = star;
  }




}
