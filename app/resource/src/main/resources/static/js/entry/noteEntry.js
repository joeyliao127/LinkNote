const $ = require( "jquery" );
const {TokenService} = require("@unityJS/TokenService");
const {NoteRender} = require("@noteJS/NoteRender");
import {EditorMediator} from "@noteJS/EditorMediator";

$(document).ready(async () => {
  await verifyToken();

  const noteId = $(location).attr('href').split("/").pop();
  const notebookId = $(location).attr('href').split("/")[4];
  const email = localStorage.getItem("email");
  const username = localStorage.getItem("username");

  //渲染note頁面
  const noteRender = new NoteRender(notebookId, noteId);
  noteRender.init();

  //連線WebSocket broker
  const editorMediator = new EditorMediator(noteId, notebookId, username, email);
  // TODO 練線websocket時要開啟
  editorMediator.wsConnector.connect();
  editorMediator.editorHanlder.generateEditor();
})

async function verifyToken () {
  const tokenService = new TokenService();
  const validateTokenResult = await tokenService.verifyUserToken();

  if(!validateTokenResult) {
    location.href = "/";
  }
}