package com.joeyliao.linknoteresource.service;

import com.joeyliao.linknoteresource.dao.NoteDAO;
import com.joeyliao.linknoteresource.dto.note.NoteDTO;
import com.joeyliao.linknoteresource.enums.collaboration.BrokerMessageType;
import com.joeyliao.linknoteresource.enums.collaboration.OperationType;
import com.joeyliao.linknoteresource.pojo.coEdit.EditPosition;
import com.joeyliao.linknoteresource.pojo.coEdit.EditPositionVersion;
import com.joeyliao.linknoteresource.pojo.coEdit.NoteContent;
import com.joeyliao.linknoteresource.pojo.coEdit.NoteHistory;
import com.joeyliao.linknoteresource.pojo.websocket.ReceivedOperationMessage;
import com.joeyliao.linknoteresource.pojo.websocket.SendOperationMessage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CoEditServiceImpl implements CoEditService{
  private final NoteDAO noteDAO;
  private final Map<String, NoteHistory> noteContainer = new HashMap<>();
  @Autowired
  public CoEditServiceImpl(NoteDAO noteDAO) {
    this.noteDAO = noteDAO;
  }

  @Override
  public SendOperationMessage getTransformOperation(ReceivedOperationMessage receivedOperationMessage) {
    String noteId = receivedOperationMessage.getNoteId();
    String clientSideVersionId = receivedOperationMessage.getVersionId();
    String serverSideVersionId = this.noteContainer.get(noteId).getLatestVersionId();
    EditPosition editPosition;
    /*
     * 流程
     * 比較版本，如果是最新版，直接merge，如果不是最新版，則針對該版本的position進行比較操作
     * 最新版:
     *  position = 指定操作位址
     * OT:
     *  轉換操作位址
     *
     * 接著完成：
     *  判斷insert還是delete
     *  進行文本更新操作
     *  更新lastEditPosition和nextEditPosition
     */

    if(clientSideVersionId.equals(serverSideVersionId)) {
      editPosition = receivedOperationMessage.getPosition();
    } else {
      editPosition = this.transformOperatingPosition(receivedOperationMessage);
    }

    this.merge(receivedOperationMessage);
//    this.calculateUpdatedPosition(receivedOperationMessage.getContent(), receivedOperationMessage.getPosition());

    return this.generateSendOperatingMessage(receivedOperationMessage, editPosition);
  }

  private void merge(ReceivedOperationMessage receivedOperationMessage) {
    log.info("執行merge");
    String noteId = receivedOperationMessage.getNoteId();
    OperationType operationType = receivedOperationMessage.getOperationType();
    EditPosition editPosition = receivedOperationMessage.getPosition();

    // 最新的字串
    ArrayList<String> latestNoteContent = this.noteContainer.get(noteId).getNoteContent();

    ArrayList<String> updateContent = null;
    ArrayList<Integer> nextPosition = null;
    String content = receivedOperationMessage.getContent();
    if(operationType == OperationType.INSERT) {
      Map<String, Object> result = this.insertText(latestNoteContent, content, editPosition);
      updateContent = (ArrayList<String>) result.get("latestContent");
      nextPosition = (ArrayList<Integer>) result.get("nextPosition");
    } else if (operationType == OperationType.DELETE) {
      Map<String, Object> result = this.deleteText(latestNoteContent, editPosition);
      updateContent = (ArrayList<String>) result.get("latestContent");
      nextPosition = (ArrayList<Integer>) result.get("nextPosition");
    }

    this.noteContainer.get(noteId).getEditHistory().get(receivedOperationMessage.getVersionId()).setNextInsertPosition(nextPosition);
    //TODO 操作完後要更新nextPosition

    this.noteContainer.get(noteId).setNoteContent(updateContent);

    log.info("更新後的noteContent: " + this.noteContainer.get(noteId).getNoteContent());
  }


  private EditPosition transformOperatingPosition(ReceivedOperationMessage receivedOperationMessage) {
    //TODO 需要根據指定版本更新的position，比較位址是否相同，
    //TODO 如果不同，直接merge，如果相同，就要將位址往後推一位，也要考慮操作的字元總數，如新增ss，那row的position就要加上2 + 1， 2是ss，+ 1才是真正的往後推一位。
    //TODO 但要考慮多行的狀況
    //TODO 現在的問題在於，如何取得noteId底下的版本資訊 1. 新增versionIdMap，根據versionId當作key，value則是記錄操作座標與結束座標。
    //TODO 為了避免Race condition，因此需要使用concurrentHashMap，避免在多個操作要更新position時，發生競爭問題。
    //TODO 有任何操作，都要更新latestVersionId, noteContent，而concurrentHashMap則是在merge或ot時才需要更新。
    //TODO 有操作的情況 - 1. merge 2.transformation 3. (ok)第一次取得noteContent(產生第一版)
    log.info("執行OT轉換");
    String noteId = receivedOperationMessage.getNoteId();
    String versionId = receivedOperationMessage.getVersionId();
    EditPosition editPosition = new EditPosition();
    EditPositionVersion editPositionVersion = this.noteContainer.get(noteId).getEditHistory().get(versionId);
    editPosition.setStartPosition(editPositionVersion.getNextInsertPosition());
    return editPosition;
  }

  private Map<String, Object> insertText(ArrayList<String> latestContent, String content, EditPosition editPosition) {

    // 取得插入座標
    Integer rowPosition = editPosition.getStartPosition().get(0);
    Integer columnPosition = editPosition.getStartPosition().get(1);
    ArrayList<Integer> nextPosition = new ArrayList<>();

    if(content.equals("\n")) {
      latestContent.add(rowPosition, "");
      nextPosition.add(0, rowPosition + 1);
      nextPosition.add(1, columnPosition);
    } else {
      ArrayList<String> splitContent = new ArrayList<>(Arrays.asList(content.split("\n")));
      // 取得server最新的content，並取得row指定index的String
      String rowContent = latestContent.get(rowPosition - 1); // 取得row欄的String，-1 是因為前端返回的座標是從1開始，不是0

      String newContent = getString(rowContent, splitContent, columnPosition);
      latestContent.set(rowPosition - 1, newContent);

      nextPosition.add(0,rowPosition + splitContent.size());
      nextPosition.add(1, columnPosition);
      //TODO 多行插入的nextStartColumn要更新
//      // 如果是多行，把剩下的都插入
      for (int i=1; i<splitContent.size(); i++) {
        latestContent.set(rowPosition + 1, splitContent.get(i));
      }
    }

    return Map.of("latestContent", latestContent, "nextPosition", nextPosition);
  }

  private static String getString(String rowContent, ArrayList<String> splitContent,
      Integer columnPosition) {
    String newContent;

    if(rowContent.isEmpty()) {
      newContent = splitContent.get(0);
    } else {
//      String startSubString = rowContent.substring(0, columnPosition - 1);
//      String insertString = splitContent[0];
//      String otherString = rowContent.substring(columnPosition - 1);
//      String res = startSubString + insertString + otherString;
      // 類似這樣 ABCE ，插入D
      // ABC + D + E
      newContent = rowContent.substring(0, columnPosition - 1) + splitContent.get(0) + rowContent.substring(rowContent.length() - 1);
    }
    return newContent;
  }

  private Map<String, Object> deleteText(ArrayList<String> latestContent, EditPosition editPosition) {

    // 取得更新的座標
    Integer startRowPosition = editPosition.getStartPosition().get(0);
    Integer startColumnPosition = editPosition.getStartPosition().get(1);
    Integer endRowPosition = editPosition.getEndPosition().get(0);
    Integer endColumnPosition = editPosition.getEndPosition().get(1);

    // 取得server最新的content，並取得指定row的String
    String rowContent = latestContent.get(startRowPosition - 1); // 取得row欄的String，-1 是因為前端返回的座標是從1開始，不是0

    // 開始刪除指定欄位
    String updatedContent = "";

    // 比如從中間刪除，後面剩下的就是substring，比如ABC，刪除B，substring就是C。
    String substring = rowContent.substring(startColumnPosition - 1, rowContent.length() - 1);
    ArrayList<Integer> nextPosition = new ArrayList<>();
    // 如果在同一行
    if(startRowPosition.equals(endRowPosition)) {
      updatedContent = rowContent.substring(0, startColumnPosition - 1) + substring;
      nextPosition.add(0, endRowPosition);
      nextPosition.add(1, endColumnPosition);
    }

    //TODO 多行刪除還沒做
    latestContent.set(startRowPosition - 1, updatedContent);

    return Map.of("latestContent", latestContent, "nextPosition", nextPosition);

  }

  private SendOperationMessage generateSendOperatingMessage(ReceivedOperationMessage message, EditPosition position) {
    SendOperationMessage sendOperationMessage = new SendOperationMessage();
    sendOperationMessage.setOperationType(message.getOperationType());
    sendOperationMessage.setUsername(message.getUsername());
    sendOperationMessage.setEmail(message.getEmail());
    sendOperationMessage.setContent(message.getContent());
    sendOperationMessage.setType(BrokerMessageType.SEND);
    sendOperationMessage.setVersionId(this.generateVersionId());
    sendOperationMessage.setPosition(position);
    return sendOperationMessage;
  }

  @Override
  public NoteContent getNoteContent(String noteId) {
    if(this.noteContainer.containsKey(noteId)) {
      ArrayList<String> splitNoteContent = this.noteContainer.get(noteId).getNoteContent();
      String noteContent = String.join("\n", splitNoteContent);
      String latestVersionId = this.noteContainer.get(noteId).getLatestVersionId();
      return new NoteContent(noteContent, latestVersionId);
    } else {
      NoteDTO noteDTO = this.noteDAO.getNote(noteId);
      String noteContent = noteDTO.getContent();
      String[] splitNoteContent = noteContent.split("\n");
      ArrayList<String> noteContentList = new ArrayList<>(Arrays.asList(splitNoteContent));
      String versionId = this.generateVersionId();
      NoteHistory noteHistory = new NoteHistory(versionId, noteContentList);
      EditPositionVersion editPositionVersion = new EditPositionVersion();
      ArrayList<Integer> position = new ArrayList<>();
      position.add(0, 0);
      position.add(1, 0);
      editPositionVersion.setNextInsertPosition(position);
      noteHistory.getEditHistory().put(versionId, editPositionVersion);

      log.info("分割後的content");
      log.info(Arrays.toString(splitNoteContent));

      this.noteContainer.put(noteId, noteHistory);
      return new NoteContent(noteContent, versionId);
    }
  }

  private String generateVersionId() {
    return UUID.randomUUID().toString().substring(24);
  }
}
