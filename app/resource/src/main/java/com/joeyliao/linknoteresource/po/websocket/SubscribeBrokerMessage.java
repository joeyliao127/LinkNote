package com.joeyliao.linknoteresource.po.websocket;

import java.lang.reflect.Array;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeBrokerMessage extends BrokerType {
  private String username;
  private String email;
  private ArrayList<String> users;
}
