import {RequestHandler} from "@unityJS/RequestHandler";

export class TokenService {

  requestHandler;
  constructor() {
    this.requestHandler = new RequestHandler();
  }

  // 回傳boolean
  async verifyUserToken() {
    //TODO 開發用的url，正式機要移除 domain ;
    const domain = "http://127.0.0.1:8080";
    const path = domain + "/api/auth/user/token";
    const token = localStorage.getItem("token");

    if (token !== null) {
      return await this.requestHandler.sendRequestWithToken(path, "POST", null);
    }
  }
}