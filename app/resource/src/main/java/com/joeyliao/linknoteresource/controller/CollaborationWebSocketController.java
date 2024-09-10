package com.joeyliao.linknoteresource.controller;

import com.joeyliao.linknoteresource.mq.EditProducer;
import com.joeyliao.linknoteresource.mq.CoEditQueueHandler;
import com.joeyliao.linknoteresource.po.websocket.SendOperationMessage;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class CollaborationWebSocketController {

  private final EditProducer producer;

  @Autowired
  public CollaborationWebSocketController(EditProducer producer) {
    this.producer = producer;
  }

  //發送訊息path，前綴為app`
  @MessageMapping("/message/{noteId}")
  //廣播的目的
  @SendTo("/collaboration/{noteId}")
  public SendOperationMessage sendMessage(
      @Payload SendOperationMessage sendOperationMessage,
      SimpMessageHeaderAccessor headerAccessor
  ) {
    //TODO 這邊要注入 OT 算法
    log.info("sendMessage: " + sendOperationMessage);
    log.info("username: " + sendOperationMessage.getUsername());
    log.info("email: " + sendOperationMessage.getEmail());
    log.info("content: " + sendOperationMessage.getContent());
    log.info("操作: " + sendOperationMessage.getOperationType());
    log.info("type: " + sendOperationMessage.getType());
    log.info("position: " + sendOperationMessage.getPosition());
    return sendOperationMessage;
  }
}
