package com.joeyliao.linknoteresource.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.joeyliao.linknoteresource.mq.EditProducer;
import com.joeyliao.linknoteresource.pojo.websocket.ReceivedOperationMessage;
import com.joeyliao.linknoteresource.pojo.websocket.SendOperationMessage;
import com.joeyliao.linknoteresource.service.CoEditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CollaborationWebSocketController {

  private final EditProducer producer;

  private final CoEditService coEditService;

  @Autowired
  public CollaborationWebSocketController(EditProducer producer, CoEditService coEditService) {
    this.producer = producer;
    this.coEditService = coEditService;
  }

  //發送訊息path，前綴為app`
  @MessageMapping("/message/{noteId}")
  @SendTo("/collaboration/{noteId}")
  public SendOperationMessage sendMessage(
      @Payload ReceivedOperationMessage receivedOperationMessage,
      SimpMessageHeaderAccessor headerAccessor
  ) throws JsonProcessingException {
    log.info("--------------Operation--------------");
    log.info("接收到的資料: " + receivedOperationMessage.toString());
    //TODO 用MQ發送 return 改為void，用ws發送return 改為receivedOperationMessage
//    this.producer.sendMessage(receivedOperationMessage.getNoteId(), receivedOperationMessage);

    return this.coEditService.getTransformOperation(receivedOperationMessage);
  }
}
