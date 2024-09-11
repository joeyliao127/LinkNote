package com.joeyliao.linknoteresource.mq;

import com.joeyliao.linknoteresource.pojo.websocket.ReceivedOperationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EditProducer {


  private final AmqpTemplate amqpTemplate;

  @Autowired
  public EditProducer (AmqpTemplate amqpTemplate) {
    this.amqpTemplate = amqpTemplate;
  }

  // TODO Content之後要改為SendOperationMessage物件，這邊先測試用
  public void sendMessage(String noteId, ReceivedOperationMessage message) {
    String exchange = "CO_EDIT_EXCHANGE";
    String routingKey = "note_queue_" + noteId;
    log.info("Producer接收訊息，準備發送");
    this.amqpTemplate.convertAndSend(exchange, routingKey, message);
  }
}
