import SockJS from "sockjs-client";
import {Client} from "@stomp/stompjs";
import {CollaborationEventHandler} from "@webSocketJS/CollaborationEventHandler";

export class CollaborationWebSocket {

  stompClient = null;
  editorHandler;

  constructor(noteId, username, email) {
    this.noteId = noteId;
    this.username = username;
    this.email = email;
    this.operationType = {
      insert: "INSERT",
      delete: "DELETE",
    }
    this.collaborationEventHandler = new CollaborationEventHandler(noteId, username, email);
    this.collaborationEventHandler.init();
  }

  setEditorHandler = (editorHandler) => {
    this.collaborationEventHandler.setEditorHandler(editorHandler);
  }

  connect = () => {
    const socket = new SockJS("/ws");
    const headers = {
      "username": this.username,
      "email": this.email,
      "noteId": this.noteId,
    };

    const stompClient = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      connectHeaders: headers,
      disconnectHeaders: headers,
      onConnect: (frame) => this.onConnected(frame, stompClient),
      onStompError: (frame) => this.collaborationEventHandler.connectFailed(),
    });
    this.stompClient = stompClient;
    stompClient.activate();
  }

  // 成功連線ws server後才會訂閱broker
  onConnected = (frame) => {
    const headers = {
      "username": this.username,
      "email": this.email,
      "noteId": this.noteId
    };
    this.stompClient.subscribe("/collaboration/" + this.noteId, this.collaborationEventHandler.receivedBrokerMessage, headers);
  }

  /**
   * @param message String
   * @param position Array<Array<number>>
   * @param operationType "INSERT", "DELETE"
   */
  sendInsertMessage = (message, position, version) => {
    console.log("按下的按鍵: ", message);
    const payload = {
      type: "SEND",
      position: position,
      operationType: this.operationType.insert,
      content: message,
      email: this.email,
      username: this.username,
      noteId: this.noteId,
      version: version,
    }

    this.stompClient.publish({
      destination: "/app/message/" + this.noteId,
      body: JSON.stringify(payload),
    })
  }

  sendDeleteMessage = (position) => {
    const payload = {
      type: "SEND",
      position: position,
      operationType: this.operationType.delete,
      email: this.email,
      username: this.username,
    }

    this.stompClient.publish({
      destination: "/app/message/" + this.noteId,
      body: JSON.stringify(payload),
    })
  }
}
