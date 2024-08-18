import {NotebookComponentGenerator} from "@notebookJS/NotebookComponentGenerator";
import iziToast from "izitoast";
import 'izitoast/dist/css/iziToast.min.css';
import Swal from "sweetalert2";

const $ = require("jquery");

export class NotebookMainRender {

  requestHandler;
  mainComponents;
  notebookComponentGenerator;

  constructor(requestHandler, notebookComponentGenerator) {
    this.notebookComponentGenerator = new NotebookComponentGenerator();
    this.notebookComponentGenerator = notebookComponentGenerator;
    this.requestHandler = requestHandler;
    this.mainComponents = {
      "createNotebookForm": $(""),
      "myNotebookArea": $(""),
      "coNotebookArea": $(""),
      "invitationsForm": $(""),
      "settingForm": $(".js_user_profile_wrapper"),
    }
  }

  async init() {
    this.buttonEventRegister();

    //TODO 渲染localstorage中的lastOpenNotebookId，如果沒有則渲染All notebook
  }

  displayMainComponent(displayComponentName) {
    Object.entries(this.mainComponents).forEach(
        ([componentName, component]) => {
          if (componentName === displayComponentName) {
            component.removeClass("display-none");
          }
          //TODO 記得要打開，等main component都移植到html檔案中
          // } else {
          //   component.addClass("display-none");
          // }
        })
  }

  buttonEventRegister() {
    $(".js_update_user_profile_btn").on('click', this.submitSettingForm);
  }

  submitSettingForm = async () => {
    const username = $(".js_setting_username").val();
    const password = $(".js_setting_password").val();
    // 驗證username & password
    if (!(username && password)) {
      iziToast.error({
        "title": "Error",
        "message": "Username or password cannot be empty.",
        "position": "topRight"
      })
      return;
    }
    //TODO 開發用的url，正式機要移除 domain ;
    const domain = "http://127.0.0.1:8080";
    const path = domain + "/api/user";
    const email = localStorage.getItem("email");
    const requestBody = {username, password, email};
    const response = await this.requestHandler.sendRequestWithToken(path, "PUT", requestBody);

    if(response.ok) {
      localStorage.setItem("username", username);
      Swal.fire({
        title: 'Success!',
        text: 'Update user profile success! Please sign out and sign in again.',
        icon: 'success',
        confirmButtonText: 'OK'
      }).then((result) => {
        if (result.isConfirmed) {
          localStorage.removeItem("token");
          window.location.href = "/";
        }
      })
    } else {
      iziToast.error({
        "title": "Error",
        "message": "Update user profile failed!",
        "position": "topRight"
      })
    }
  }

}