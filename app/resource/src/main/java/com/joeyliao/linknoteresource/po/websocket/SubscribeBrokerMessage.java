package com.joeyliao.linknoteresource.po.websocket;

import com.joeyliao.linknoteresource.enums.collaboration.BrokerMessageType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubscribeBrokerMessage extends BrokerType {
  private String username;
  private String email;
}
