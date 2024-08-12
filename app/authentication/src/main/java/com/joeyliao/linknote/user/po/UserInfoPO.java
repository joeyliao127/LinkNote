package com.joeyliao.linknote.user.po;

import lombok.Data;

@Data
public class UserInfoPO {
  //用於token中payload所許資訊
  private String username;
  private String userId;
  private String email;
  private Boolean status;
}
