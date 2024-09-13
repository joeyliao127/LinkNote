package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.pojo.coEdit.NoteContent;
import com.joeyliao.linknoteresource.pojo.websocket.ReceivedOperationMessage;
import com.joeyliao.linknoteresource.pojo.websocket.SendOperationMessage;
import java.util.Map;

public interface CoEditService {
  SendOperationMessage getTransformOperation(ReceivedOperationMessage receivedOperationMessage);

  NoteContent getNoteContent(String noteId);

}
