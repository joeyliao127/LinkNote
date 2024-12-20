package com.joeyliao.linknoteresource.eventlistener;

import com.joeyliao.linknoteresource.enums.collaboration.BrokerMessageType;
import com.joeyliao.linknoteresource.pojo.coEdit.NoteContent;
import com.joeyliao.linknoteresource.pojo.websocket.DisconnectedBrokerMessage;
import com.joeyliao.linknoteresource.pojo.websocket.SubscribeBrokerMessage;
import com.joeyliao.linknoteresource.service.CoEditQueueService;
import com.joeyliao.linknoteresource.service.CoEditService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class CollaborationEventListener {

  private final SimpMessageSendingOperations messagingTemplate;
  private final CoEditQueueService coEditQueueService;
  private final CoEditService coEditService;

  private Map<String, Map<String, String>> user = new ConcurrentHashMap<>();
  private ArrayList<String> userList = new ArrayList<>();



  @Autowired
  public CollaborationEventListener(CoEditQueueService coEditQueueService,
      SimpMessageSendingOperations messagingTemplate, CoEditService coEditService) {
    this.coEditQueueService = coEditQueueService;
    this.messagingTemplate = messagingTemplate;
    this.coEditService = coEditService;
  }

  //建立連線事件
  @EventListener
  public void connectEvent(SessionConnectEvent event) {
    log.info("連線事件: \n" + event.getMessage());
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    log.info("使用者：" + headerAccessor.getFirstNativeHeader("username"));
    log.info("email: " + headerAccessor.getFirstNativeHeader("email"));
    log.info("sessionId: " + headerAccessor.getSessionId());
    String username = headerAccessor.getFirstNativeHeader("username");
    String email = headerAccessor.getFirstNativeHeader("email");
    String noteId = headerAccessor.getFirstNativeHeader("noteId");
    String sessionId = headerAccessor.getSessionId();
    this.putUser(sessionId, username, email, noteId);
    log.info("===================");
  }

  @EventListener
  public void disconnectBrokerEvent(SessionDisconnectEvent event) {
    log.info("連線斷開事件: \n" + event.getMessage());
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();
    DisconnectedBrokerMessage message = new DisconnectedBrokerMessage();
    Map<String, String> user =this.findUsersBySessionId(sessionId);

    String email = user.get("email");
    String noteId = user.get("noteId");
    this.removeUser(email);

    message.setType(BrokerMessageType.DISCONNECT);
    message.setUsername(user.get("username"));
    message.setEmail(email);
    message.setUsers(this.userList);

    messagingTemplate.convertAndSend("/collaboration/" + noteId, message);

    //TODO 注入NoteContainer，判斷連線人數是否為0，如果是則刪除queue。
    //TODO 如果人數為0，清除noteContainer中指定noteId的資料
    log.info("username: " + user.get("username"));
    log.info("email: " + user.get("email"));
    log.info("noteId: " + noteId);
    log.info("===================");
  }


  @EventListener
  public void subscribeBrokerEvent(SessionSubscribeEvent event) {
    //從event中取得header，方便調用STOMP中的header參數
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();
    Map<String, String> user = findUsersBySessionId(sessionId);

    SubscribeBrokerMessage message = new SubscribeBrokerMessage();
    String noteId = user.get("noteId");
    String email = user.get("email");
    String username = user.get("username");
    NoteContent noteContent = this.coEditService.getNoteContent(noteId);
    this.appendUser(email);

    message.setUsername(username);
    message.setEmail(user.get("email"));
    message.setType(BrokerMessageType.SUBSCRIBE);
    message.setUsers(this.userList);
    message.setNoteContent(noteContent.getNoteContent());
    message.setVersionId(noteContent.getVersionId());

    messagingTemplate.convertAndSend("/collaboration/" + noteId, message);
    log.info("訂閱事件");
    log.info("username: " + username);
    log.info("email: " + user.get("email"));
    log.info("noteId: " + noteId);
    log.info("===================");

    if(this.coEditQueueService.isNotQueueExist(noteId)) {
      this.coEditQueueService.createNoteQueue(noteId);
    }
  }

  private void putUser(String sessionId, String username, String email, String noteId) {
    Map<String, String> map = new HashMap<>();
    map.put("email", email);
    map.put("username", username);
    map.put("noteId", noteId);
    this.user.put(sessionId, map);
  }

  private Map<String, String> findUsersBySessionId(String sessionId) {
    return this.user.get(sessionId);
  }

  private void appendUser(String email) {
    this.userList.add(email);
  }

  private void removeUser(String email) {
    this.userList.remove(email);
  }
}
