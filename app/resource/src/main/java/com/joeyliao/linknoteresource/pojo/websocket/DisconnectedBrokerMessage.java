package com.joeyliao.linknoteresource.pojo.websocket;

import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisconnectedBrokerMessage extends BrokerType {
  private String username;
  private String email;
  private ArrayList<String> users;
}
