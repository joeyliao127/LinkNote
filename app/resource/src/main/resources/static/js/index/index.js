import {TokenService} from "@unityJS/TokenService";
import {Validator} from "@unityJS/Validator";
import {RequestHandler} from "@unityJS/RequestHandler";

const $ = require("jquery");

export class Index {

  tokenService;

  validator;

  requestHandler;

  constructor() {
    this.tokenService = new TokenService();
    this.validator = new Validator();
    this.requestHandler = new RequestHandler();
  }

  init() {
    this.redirectToNotebooksIfTokenValid();
    this.switchSignInAndRegisterForm();
    this.listenSubmitRegisterEvent();
  }

  redirectToNotebooksIfTokenValid() {
    if (localStorage.getItem("token")) {
      this.tokenService.verifyUserToken().then((result) => {
        if (result) {
          location.href = "/notebooks";
        }
      });
    }
  }

  //切換登入與註冊表單事件監聽
  switchSignInAndRegisterForm() {
    $("#toSignin").on('click', function () {
      $(".signin-ctn").removeClass("display-none");
      $(".signup-ctn").addClass("display-none");
    });

    $("#toSignup").on("click", () => {
      $(".signin-ctn").addClass("display-none");
      $(".signup-ctn").removeClass("display-none");
    });
  }

  //監聽註冊表單
  listenSubmitRegisterEvent() {
    $(".signup-ctn .form-ctn button").on("click", async () => {
      const email = $("#signup-email").val();
      const username = $("#username").val();
      const password = $("#signup-password").val();
      const confirmPassword = $("#confirmPassword").val();
      const validResult = this.validParams(username, email, password, confirmPassword);

      if (!validResult.result) {
        this.displayErrorMsg($("#signup-error-msg"), validResult.message);
        return false;
      }

      const response = await this.submitRegisterForm(username, email, password);
      switch (response.status) {
        case 201:
          const token = await response.json();
          localStorage.setItem("token", token);
          window.alert("Success!");
          window.location.href = "/notebooks";
          break;
        case 400:
          this.displayErrorMsg($("#signup-error-msg"),
              "Email already exist.");
          break;
        default:
          this.displayErrorMsg($("#signup-error-msg"),
              "Something went wrong. Please try again later.");
          break;
      }
    });
  }

  validParams(username, email, password, confirmPassword) {

    if(!username) {
      return {
        result: false,
        message: "Please enter your username."
      };
    }

    if (!this.validator.validateEmailFormat(email)) {
      return {
        result: false,
        message: "Please enter the correct format for your email."
      };
    }

    if(password === "") {
      return {
        result: false,
        message: "Please enter your password."
      };
    }

    //驗證密碼和再次確認密碼是否相同
    if (password !== confirmPassword) {
      return {
        result: false,
        message: "Please ensure that your passwords match."
      };
    }

    return {result: true};
  }

  async submitRegisterForm(username, email, password) {
    //TODO 開發用的url，正式機要移除 domain ;
    const domain = "http://127.0.01:8080";
    const path = domain + "/api/user/register";
    const requestBody = {username, email, password};
    return await this.requestHandler.sendRequestWithToken(path, "POST", requestBody);
  }

  displayErrorMsg(msgElement, message) {
    msgElement.text(message);
    setTimeout(() => {
      msgElement.text("");
    }, 5000);
  }
}