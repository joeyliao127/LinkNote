package com.joeyliao.linknoteresource.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EditConsumer {

  //TODO 這邊要想如何動態取得queue名稱(noteId)
  @RabbitListener(queues = "DIRECT_QUEUE")
  public void listen(String content) {
    log.info("=========接收MQ中的訊息==========");
    log.info("content: " + content);
    //TODO OT算法在這邊實現
  }

}
