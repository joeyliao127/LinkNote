package com.joeyliao.linknoteresource.mq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.joeyliao.linknoteresource.pojo.websocket.ReceivedOperationMessage;
import com.joeyliao.linknoteresource.pojo.websocket.SendOperationMessage;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EditConsumer {

  private final ObjectMapper objectMapper;

  private final SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  public EditConsumer(ObjectMapper objectMapper, SimpMessagingTemplate simpMessagingTemplate) {
    this.objectMapper = objectMapper;
    this.simpMessagingTemplate = simpMessagingTemplate;
  }

//  @SendTo("/collaboration/{noteId}")
  public void handleMessage(byte[] byteMessage) throws IOException {

    ReceivedOperationMessage receivedOperationMessage = this.objectMapper.readValue(byteMessage, ReceivedOperationMessage.class);
    log.info("=========接收MQ中的訊息==========");
    log.info("content: " + receivedOperationMessage.toString());

    String noteId = receivedOperationMessage.getNoteId();

    this.simpMessagingTemplate.convertAndSend("/collaboration/" + noteId, receivedOperationMessage);
    //TODO OT算法在這邊實現
  }

}
