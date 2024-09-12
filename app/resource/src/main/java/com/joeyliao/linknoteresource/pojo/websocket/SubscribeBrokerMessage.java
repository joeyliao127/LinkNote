package com.joeyliao.linknoteresource.pojo.websocket;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeBrokerMessage extends BrokerType {
  private String username;
  private String email;
  private String noteContent;
  private String versionId;
  private ArrayList<String> users;
}
