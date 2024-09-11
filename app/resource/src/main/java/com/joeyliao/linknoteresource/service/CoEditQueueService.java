package com.joeyliao.linknoteresource.service;


import com.joeyliao.linknoteresource.mq.EditConsumer;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CoEditQueueService {

  private final RabbitAdmin rabbitAdmin;
  private final DirectExchange directExchange;

  private final RabbitListenerEndpointRegistry registry;
  private final EditConsumer consumer;

  private final RabbitListenerContainerFactory<?> rabbitListenerContainerFactory;

  @Autowired
  public CoEditQueueService(RabbitAdmin rabbitAdmin, DirectExchange directExchange,
      RabbitListenerEndpointRegistry registry, EditConsumer consumer,
      RabbitListenerContainerFactory<?> rabbitListenerContainerFactory) {
    this.rabbitAdmin = rabbitAdmin;
    this.directExchange = directExchange;
    this.registry = registry;
    this.consumer = consumer;
    this.rabbitListenerContainerFactory = rabbitListenerContainerFactory;
  }

  public void createNoteQueue(String noteId) {
    String queueName = "note_queue_" + noteId;
    Queue queue = new Queue(queueName, true, true, true);
    this.rabbitAdmin.declareQueue(queue);
    this.bindingExchange(queue);
    this.registryConsumer(queue);
  }

  private void bindingExchange(Queue queue) {
    String routingKey = queue.getName();
    Binding binding = BindingBuilder.bind(queue).to(directExchange).with(routingKey);
    this.rabbitAdmin.declareBinding(binding);
  }

  private void registryConsumer(Queue queue) {
    String queueName = queue.getName();
    MessageListenerAdapter listenerAdapter = new MessageListenerAdapter(this.consumer, "handleMessage");

    SimpleRabbitListenerEndpoint endpoint = new SimpleRabbitListenerEndpoint();
    endpoint.setId(queueName);
    endpoint.setQueueNames(queueName);
    endpoint.setMessageListener(listenerAdapter);

    registry.registerListenerContainer(endpoint, this.rabbitListenerContainerFactory, true);
  }
}
