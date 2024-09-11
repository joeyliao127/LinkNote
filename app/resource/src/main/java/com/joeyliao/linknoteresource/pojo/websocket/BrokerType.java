package com.joeyliao.linknoteresource.pojo.websocket;

import com.joeyliao.linknoteresource.enums.collaboration.BrokerMessageType;
import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BrokerType {
  private BrokerMessageType type;
}
