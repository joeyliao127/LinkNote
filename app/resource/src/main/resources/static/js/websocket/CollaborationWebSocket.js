import SockJS from "sockjs-client";
import {Client} from "@stomp/stompjs";
import $ from "jquery";

export class CollaborationWebSocket {

  stompClient = null;

  constructor(noteId, username, email) {
    this.noteId = noteId;
    this.username = username;
    this.email = email;
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
      onStompError: (frame) => this.connectFailed(),
    });
    this.stompClient = stompClient;
    stompClient.activate();
  }

  // 成功連線ws server後才會訂閱broker
  onConnected = (frame) => {
    console.log("連線成功");
    console.log(frame);
    const headers = {
      "username": this.username,
      "email": this.email,
      "noteId": this.noteId
    };
    this.stompClient.subscribe("/collaboration/" + this.noteId, this.receivedBrokerMessage, headers);
    this.submitTextEvent(this.stompClient);
  }

  connectFailed = () => {
    console.log("連線失敗");
  }

  // 接收來自後端的broker的訊息
  receivedBrokerMessage = (message) => {
    console.log("收到來自後端的訊息");
    const data = JSON.parse(message.body);
    console.log("原始data");
    console.log(data);
    const type = data.type;

    switch (type) {
      case "SUBSCRIBE":
        console.log("SUBSCRIBE EVENT");
        break;
      case "DISCONNECT":
        console.log("DISCONNECT");
        break;
      case "SEND":
        const p = document.createElement("p");
        p.textContent = `${data.username}: ${data.content}`;
        $(`.msgBoard`).append(p);
        break;
      default:
        console.log("Default");
        break;
    }

  }

  submitTextEvent = (stompClient) => {
    $(".submitText").on("click", (e) => {
      e.preventDefault();
      this.sendMessage("Hi", 1, "INSERT");
    })
  }

  sendMessage = (message, position, operationType) => {
    const payload = {
      type: "SEND",
      position: position,
      operationType: operationType,
      content: message,
      email: this.email,
      username: this.username,
    }

    this.stompClient.publish({
      destination: "/app/message/" + this.noteId,
      body: JSON.stringify(payload),
    })
  }

}
