package com.joeyliao.linknoteresource.mq;

import com.joeyliao.linknoteresource.service.CoEditQueueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EditConsumer {

  //TODO 這邊要想如何動態取得queue名稱(noteId)
  //TODO void要改為OT算法的結果Object
  @SendTo("/collaboration/{noteId}")
  public void handleMessage(String content) {
    log.info("=========接收MQ中的訊息==========");
    log.info("content: " + content);
    //TODO OT算法在這邊實現
  }

}
