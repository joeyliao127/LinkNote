package com.linknote.online.linknotespring.note.notedto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Valid
public class CreateNotebookParamsDTO {
  @NotBlank
  private String name;

  private List<String> tags;
  private String description;

  private List<Email> emails;
  public static class Email{
    private String email;
    private Integer emailId;

    public String getEmail() {
      return email;
    }

    public void setEmail(String email) {
      this.email = email;
    }

    public Integer getEmailId() {
      return emailId;
    }

    public void setEmailId(Integer emailId) {
      this.emailId = emailId;
    }
  }



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

  public List<Email> getEmails() {
    return emails;
  }

  public void setEmails(
      List<Email> emails) {
    this.emails = emails;
  }
}
