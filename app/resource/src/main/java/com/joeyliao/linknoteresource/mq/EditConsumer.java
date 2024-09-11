package com.joeyliao.linknoteresource.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joeyliao.linknoteresource.pojo.websocket.ReceivedOperationMessage;
import com.joeyliao.linknoteresource.pojo.websocket.SendOperationMessage;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EditConsumer {

  private final ObjectMapper objectMapper;

  @Autowired
  public EditConsumer(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  //  @SendTo("/collaboration/{noteId}")
  public void handleMessage(byte[] byteMessage) throws IOException {

    ReceivedOperationMessage receivedOperationMessage = this.objectMapper.readValue(byteMessage, ReceivedOperationMessage.class);
    log.info("=========接收MQ中的訊息==========");
    log.info("content: " + receivedOperationMessage.toString());
    //TODO OT算法在這邊實現
    SendOperationMessage sendOperationMessage = new SendOperationMessage();
  }

}
