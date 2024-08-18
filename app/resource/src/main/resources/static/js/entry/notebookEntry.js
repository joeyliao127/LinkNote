import {TokenService} from "@unityJS/TokenService";
import {SideBarRender} from "@notebookJS/sideBarRender";
import {NotebookMainRender} from "@notebookJS/NotebookMainRender";
import {RequestHandler} from "@unityJS/RequestHandler";
import {NotebookComponentGenerator} from "@notebookJS/NotebookComponentGenerator";

const $ = require( "jquery" );

$(document).ready(async () => {
  const tokenService = new TokenService();
  const validateTokenResult = await tokenService.verifyUserToken();
  if(!validateTokenResult) {
    location.href = "/";
  }

  const notebookMainRender = new NotebookMainRender(new RequestHandler(), new NotebookComponentGenerator());
  const sideBarRender = new SideBarRender(new RequestHandler(), new NotebookComponentGenerator(), notebookMainRender);

  sideBarRender.init();
  notebookMainRender.init();
})
