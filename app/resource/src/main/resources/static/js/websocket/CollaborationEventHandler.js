const $ = require( "jquery" );
import {MessageSender} from "@unityJS/MessageSender";

export class CollaborationEventHandler {
  editorHandler;

  constructor(noteId, username, email) {
    this.noteId = noteId;
    this.username = username;
    this.email = email;
    this.meesageSender = new MessageSender();
  }

  setEditorHandler = (editorHandler) => {
    this.editorHandler = editorHandler;
  }

  init() {
    this.setOwnerEmail();
  }

  // 在共編者表單中的Owner加上data屬性
  setOwnerEmail = () => {
    $(`.js_owner_name`).attr("data-email", this.email);
  }

  receivedBrokerMessage = (message) => {
    const data = JSON.parse(message.body);
    const type = data.type;
    let notifyMessage = "";

    switch (type) {
      case "SUBSCRIBE":
        notifyMessage = `${data.username} joined.`;
        this.renderCollaborator(data, notifyMessage);
        break;
      case "DISCONNECT":
        notifyMessage = `${data.username} quit.`;
        this.renderCollaborator(data, notifyMessage);
        break;
      case "SEND":
        console.log("收到來自後端的SEND訊息");
        console.log(data);
        this.appendMessageToEditor(data);
        break;
      default:
        console.log("Default");
        break;
    }
  }
  connectFailed = () => {
    this.meesageSender.error("Cannot connect to the server.");
  }

  renderCollaborator = (userData, message) => {
    const {users} = userData;
    const joinedEmail = userData.email;
    $('.js_status_point').removeClass("online offline");
    users.forEach((user) => {
      $(`.user[data-email="${user}"]`).parent().find(
          ".js_status_point").addClass("online");
    })

    $('.js_status_point:not(.online)').addClass("offline");

    if (localStorage.getItem("email") !== joinedEmail) {
      this.meesageSender.info(message);
    }
  }

  appendMessageToEditor = (data) => {
    this.editorHandler.appendMessage(data);
  }
}