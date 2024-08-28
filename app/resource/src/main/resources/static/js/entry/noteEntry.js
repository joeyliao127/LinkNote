const $ = require( "jquery" );
const {TokenService} = require("@unityJS/TokenService");
const {NoteRender} = require("@noteJS/NoteRender");
import {CollaborationWebSocket} from "@webSocketJS/CollaborationWebSocket";
$(document).ready(async () => {
  const tokenService = new TokenService();
  const validateTokenResult = await tokenService.verifyUserToken();

  if(!validateTokenResult) {
    location.href = "/";
  }

  const noteId = $(location).attr('href').split("/").pop();
  const notebookId = $(location).attr('href').split("/")[4];
  const collaborationWebSocket = new CollaborationWebSocket(noteId, localStorage.getItem("username"), localStorage.getItem("email"));
  const noteRender = new NoteRender(notebookId, noteId);
  noteRender.init();
  collaborationWebSocket.connect();
})
