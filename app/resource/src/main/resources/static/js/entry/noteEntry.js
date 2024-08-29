const $ = require( "jquery" );
const {TokenService} = require("@unityJS/TokenService");
const {NoteRender} = require("@noteJS/NoteRender");
import {CollaborationWebSocket} from "@webSocketJS/CollaborationWebSocket";

$(document).ready(async () => {
   await verifyToken();

  const noteId = $(location).attr('href').split("/").pop();
  const notebookId = $(location).attr('href').split("/")[4];
  const email = localStorage.getItem("email");
  const username = localStorage.getItem("username");

  //連線WebSocket broker
  const collaborationWebSocket = new CollaborationWebSocket(noteId, username, email);
  collaborationWebSocket.connect();

  //渲染note頁面
  const noteRender = new NoteRender(notebookId, noteId);
  noteRender.init();
})

async function verifyToken () {
  const tokenService = new TokenService();
  const validateTokenResult = await tokenService.verifyUserToken();

  if(!validateTokenResult) {
    location.href = "/";
  }
}