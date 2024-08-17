const $ = require( "jquery" );
import {RequestHandler} from "@unityJS/RequestHandler";

export class SideBarRender {
  requestHandler;
  constructor() {
    this.requestHandler = new RequestHandler();
  }

  async init() {
    this.renderUserInfo();
  }

  async renderUserInfo() {
    //TODO 開發用的url，正式機要移除 domain ;
    const domain = "http://127.0.0.1:8080";
    const path = domain + "/api/user/info";
    const response = await this.requestHandler.sendRequestWithToken(path, "GET", null);
    const data = await response.json();
    $("#username").text(data.username);
    $("#userEmail").text(data.email);
    localStorage.setItem("email", data.email);
    localStorage.setItem("username", data.username);
  }


}