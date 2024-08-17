import {TokenService} from "@unityJS/TokenService";
import {SideBarRender} from "@notebookJS/sideBarRender";
import {NotebookMainRender} from "@notebookJS/NotebookMainRender";

const $ = require( "jquery" );

$(document).ready(async () => {
  const tokenService = new TokenService();
  const validateTokenResult = await tokenService.verifyUserToken();

  if(!validateTokenResult) {
    location.href = "/";
  }

  const sideBarRender = new SideBarRender();
  const notebookMainRender = new NotebookMainRender();
  sideBarRender.init();
  notebookMainRender.init();
})
