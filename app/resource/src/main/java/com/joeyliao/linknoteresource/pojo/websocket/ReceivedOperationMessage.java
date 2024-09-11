package com.joeyliao.linknoteresource.pojo.websocket;

import com.joeyliao.linknoteresource.enums.collaboration.OperationType;
import java.io.Serializable;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceivedOperationMessage extends BrokerType implements Serializable {
  private ArrayList<ArrayList<Integer>> position;
  private OperationType operationType;
  private String content;
  private String email;
  private String username;
  private String noteId;
}
