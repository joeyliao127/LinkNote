import {RequestHandler} from "@unityJS/RequestHandler";
import {URL} from "@unityJS/URL";

export class TokenService {

  requestHandler;
  constructor() {
    this.requestHandler = new RequestHandler();
    this.URL = new URL();
  }

  // 回傳boolean
  async verifyUserToken() {
    const path = this.URL.domain + "/api/auth/user/token";
    const token = localStorage.getItem("token");

    if (token !== null) {
      // 回傳boolean
      return await this.requestHandler.sendRequestWithToken(path, "POST", null);
    }

    return false;
  }
}