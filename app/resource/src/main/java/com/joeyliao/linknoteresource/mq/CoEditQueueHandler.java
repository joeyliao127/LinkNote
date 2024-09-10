package com.joeyliao.linknoteresource.mq;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CoEditQueueHandler {

  private final RabbitAdmin rabbitAdmin;
  private final DirectExchange directExchange;

  @Autowired
  public CoEditQueueHandler(RabbitAdmin rabbitAdmin, DirectExchange directExchange) {
    this.rabbitAdmin = rabbitAdmin;
    this.directExchange = directExchange;
  }

  public Queue createNoteQueue(String noteId) {
    Queue queue = new Queue(noteId, true, true, true);
    this.rabbitAdmin.declareQueue(queue);
    return queue;
  }

  public void bindingExchange (Queue queue) {
    String routingKey = queue.getName();
    Binding binding = BindingBuilder.bind(queue).to(directExchange).with(routingKey);
    this.rabbitAdmin.declareBinding(binding);
  }

}
