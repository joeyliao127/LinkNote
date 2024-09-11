package com.joeyliao.linknoteresource.pojo.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.joeyliao.linknoteresource.enums.collaboration.OperationType;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceivedOperationMessage extends BrokerType{

  public ReceivedOperationMessage() {}

  private ArrayList<ArrayList<Integer>> position;
  private OperationType operationType;
  private String content;
  private String email;
  private String username;
  private String noteId;

  public ReceivedOperationMessage(ArrayList<ArrayList<Integer>> position,
      OperationType operationType,
      String content, String email, String username, String noteId) {
    this.position = position;
    this.operationType = operationType;
    this.content = content;
    this.email = email;
    this.username = username;
    this.noteId = noteId;
  }

  @Override
  public String toString() {
    return "Message{" +
        "noteId=" + noteId +
        ", operationType='" + operationType + '\'' +
        ", content='" + content + '\'' +
        ", email='" + email + '\'' +
        ", username='" + username + '\'' +
        '}';
  }
}
