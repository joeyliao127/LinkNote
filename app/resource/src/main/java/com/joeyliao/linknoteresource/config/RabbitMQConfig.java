package com.joeyliao.linknoteresource.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Bean
  public DirectExchange createCoEditDirectExchange() {
    return new DirectExchange("CO_EDIT_EXCHANGE", true, true);
  }

  @Bean
  public RabbitAdmin rabbitAdmin(RabbitTemplate rabbitTemplate) {
    return new RabbitAdmin(rabbitTemplate);
  }

}
