package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.pojo.websocket.ReceivedOperationMessage;

public interface CoEditService {
  void getTransformOperation(ReceivedOperationMessage receivedOperationMessage);

  String getNoteContent(String noteId);
}
