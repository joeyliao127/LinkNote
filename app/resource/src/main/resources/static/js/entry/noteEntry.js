const $ = require( "jquery" );
const {TokenService} = require("@unityJS/TokenService");
const {NoteRender} = require("@noteJS/NoteRender");

$(document).ready(async () => {
  const tokenService = new TokenService();
  const validateTokenResult = await tokenService.verifyUserToken();
  if(!validateTokenResult) {
    location.href = "/";
  }

  const noteRender = new NoteRender();
  noteRender.init();
})
