package com.linknote.online.linknotespring.user.userpo;

public class UserInfoPO {
  //用於token中payload所許資訊
  private String username;
  private Integer userId;
  private String email;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
