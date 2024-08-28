package com.joeyliao.linknoteresource.po.websocket;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DisconnectedBrokerMessage extends BrokerType {
  private String username;
  private String email;
}
