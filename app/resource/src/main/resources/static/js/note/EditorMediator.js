import {CollaborationWebSocket} from "@webSocketJS/CollaborationWebSocket";
import {EditorHandler} from "@noteJS/EditorHandler";

export class EditorMediator {
  constructor(noteId, notebookId, username, email) {
    this.wsConnector = new CollaborationWebSocket(noteId, username, email);
    this.editorHanlder = new EditorHandler(noteId, notebookId);
    this.wsConnector.setEditorHandler(this.editorHanlder);
    this.editorHanlder.setWsConnector(this.wsConnector);
  }
}