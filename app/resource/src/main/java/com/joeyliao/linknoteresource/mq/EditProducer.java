package com.joeyliao.linknoteresource.mq;

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
  public void sendMessage(String noteId, String content) {
    String exchange = "CO_EDIT_EXCHANGE";
    this.amqpTemplate.convertAndSend(exchange, noteId, content);
  }
}
