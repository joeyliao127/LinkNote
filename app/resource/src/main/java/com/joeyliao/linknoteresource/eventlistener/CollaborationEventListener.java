package com.joeyliao.linknoteresource.eventlistener;

import com.joeyliao.linknoteresource.enums.collaboration.BrokerMessageType;
import com.joeyliao.linknoteresource.po.websocket.DisconnectedBrokerMessage;
import com.joeyliao.linknoteresource.po.websocket.SubscribeBrokerMessage;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
  private Map<String, Map<String, String>> users = new ConcurrentHashMap<>();

  @EventListener
  public void connectedEvent(SessionConnectEvent event) {
    log.info("connectedEvent: " + event.getMessage());
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
    String email = (String) Objects.requireNonNull(headerAccessor.getNativeHeader("email")).get(0);
    String sessionId = (String) headerAccessor.getSessionId();
    this.putUser(sessionId, username, email);
  }

  @EventListener
  public void disconnectBrokerEvent(SessionDisconnectEvent event) {
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();
    String noteId = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("noteId");
    DisconnectedBrokerMessage message = new DisconnectedBrokerMessage();
    Map<String, String> user =this.findUsersBySessionId(sessionId);
    message.setUsername(user.get("username"));
    message.setEmail(user.get("email"));
    messagingTemplate.convertAndSend("/collaboration/" + noteId, message);
  }


  @EventListener
  public void subscribeBrokerEvent(SessionSubscribeEvent event) {
    //從event中取得header，方便調用STOMP中的header參數
    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = headerAccessor.getSessionId();
    log.info("訂閱:");
    String noteId = Objects.requireNonNull(headerAccessor.getNativeHeader("noteId")).toString();
    Map<String, String> user = findUsersBySessionId(sessionId);
    SubscribeBrokerMessage message = new SubscribeBrokerMessage();
    String username = user.get("username");
    message.setUsername(username);
    message.setEmail(user.get("email"));
    message.setType(BrokerMessageType.SUBSCRIBE);
    messagingTemplate.convertAndSend("/collaboration/" + noteId, message);
  }

  private void putUser(String sessionId, String username, String email) {
    Map<String, String> map = new HashMap<>();
    map.put("email", email);
    map.put("username", username);
    this.users.put(sessionId, map);
  }

  private Map<String, String> findUsersBySessionId(String sessionId) {
    return this.users.get(sessionId);
  }
}
