package com.linknote.online.linknotespring.note.notedto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Valid
public class NotebookCreateParamsDTO {
  @NotBlank
  private String name;

  private List<String> tags;
  private String description;

  private List<Integer> emails;



  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getTags() {
    return tags;
  }

  public void setTags(List<String> tags) {
    this.tags = tags;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<Integer> getEmails() {
    return emails;
  }

  public void setEmails(List<Integer> emails) {
    this.emails = emails;
  }
}
