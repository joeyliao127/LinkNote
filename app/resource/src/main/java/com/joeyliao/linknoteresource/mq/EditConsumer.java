package com.joeyliao.linknoteresource.mq;

import com.joeyliao.linknoteresource.pojo.websocket.ReceivedOperationMessage;
import com.joeyliao.linknoteresource.pojo.websocket.SendOperationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EditConsumer {

//  @SendTo("/collaboration/{noteId}")
  public void handleMessage(ReceivedOperationMessage receivedMessage) {
    log.info("=========接收MQ中的訊息==========");
    log.info("content: " + receivedMessage.toString());
    //TODO OT算法在這邊實現
    SendOperationMessage sendOperationMessage = new SendOperationMessage();

//    return sendOperationMessage;
  }

}
