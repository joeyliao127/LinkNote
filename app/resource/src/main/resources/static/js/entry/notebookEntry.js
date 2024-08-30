import {TokenService} from "@unityJS/TokenService";
import {SideBarRender} from "@notebookJS/SideBarRender";
import {NotebookMainRender} from "@notebookJS/NotebookMainRender";

const $ = require( "jquery" );

$(document).ready(async () => {
  const tokenService = new TokenService();
  const validateTokenResult = await tokenService.verifyUserToken();
  if(!validateTokenResult) {
    location.href = "/";
  }

  const notebookMainRender = new NotebookMainRender();
  const sideBarRender = new SideBarRender(notebookMainRender);

  sideBarRender.init();
  notebookMainRender.init();
})
