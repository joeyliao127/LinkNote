import {TokenService} from "@unityJS/TokenService";
export class Index {

  tokenService;
  constructor() {
    this.tokenService = new TokenService();
  }

  init() {
    if (localStorage.getItem("token")) {
      this.tokenService.verifyUserToken().then((result) => {
        if (result) {
          location.href = "/notebooks";
        }
      });
    }
  }
}