package com.joeyliao.linknoteresource.pojo.websocket;

import com.joeyliao.linknoteresource.enums.collaboration.BrokerMessageType;
import com.joeyliao.linknoteresource.enums.collaboration.OperationType;
import com.joeyliao.linknoteresource.pojo.coEdit.EditPosition;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.sql.In;

@Getter
@Setter
public class SendOperationMessage extends BrokerType{
  private OperationType operationType;
  private String versionId;
  private EditPosition position;
  private String content;
  private String email;
  private String username;
}
