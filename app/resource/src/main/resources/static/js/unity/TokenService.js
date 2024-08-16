import {RequestHandler} from "@unityJS/RequestHandler";

export class TokenService {

  requestHandler;
  constructor() {
    this.requestHandler = new RequestHandler();
  }

  async verifyUserToken() {
    const path = "/api/auth/user/token";
    const token = localStorage.getItem("token");

    if (token !== null) {
      return await this.requestHandler.sendRequestWithToken(path, "POST", null);
    }
  }
}