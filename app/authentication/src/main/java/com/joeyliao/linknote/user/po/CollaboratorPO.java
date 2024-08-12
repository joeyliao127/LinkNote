package com.joeyliao.linknote.user.po;

import lombok.Data;

@Data
public class CollaboratorPO {
  private String username;
  private Integer userId;
  private String email;
  private String owner;
  private Boolean status;
}
