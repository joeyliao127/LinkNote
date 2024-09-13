package com.joeyliao.linknoteresource.pojo.websocket;

import com.joeyliao.linknoteresource.enums.collaboration.OperationType;
import com.joeyliao.linknoteresource.pojo.coEdit.EditPosition;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceivedOperationMessage extends BrokerType{

  public ReceivedOperationMessage() {}

  private EditPosition position;
  private OperationType operationType;
  private String content;
  private String email;
  private String username;
  private String noteId;
  private String versionId;

  @Override
  public String toString() {
    return "Message{" +
        "noteId=" + noteId +
        ", operationType='" + operationType + '\'' +
        ", content='" + content + '\'' +
        ", email='" + email + '\'' +
        ", username='" + username + '\'' +
        ", position='" + position + '\'' +
        ", versionId='" + versionId + '\'' +
        '}';
  }
}
