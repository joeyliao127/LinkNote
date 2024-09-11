package com.joeyliao.linknoteresource.po.websocket;

import com.joeyliao.linknoteresource.enums.collaboration.BrokerMessageType;
import com.joeyliao.linknoteresource.enums.collaboration.OperationType;
import java.util.ArrayList;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendOperationMessage extends BrokerType {
  private ArrayList<ArrayList<Integer>> position;
  private OperationType operationType;
  private String content;
  private String email;
  private String username;
  private String noteId;
}
