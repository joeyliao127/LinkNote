package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.pojo.websocket.ReceivedOperationMessage;
import java.util.Map;

public interface CoEditService {
  void getTransformOperation(ReceivedOperationMessage receivedOperationMessage);

  Map<String, Object> getNoteContentAndVersion(String noteId);

}
