const $ = require( "jquery" );

export class SideBarRender {
  requestHandler;
  notebookComponentGenerator;
  mainComponents;
  notebookMainRender;
  constructor(requestHandler, notebookComponentGenerator, notebookMainRender) {
    this.requestHandler = requestHandler;
    this.notebookComponentGenerator = notebookComponentGenerator;
    this.notebookMainRender = notebookMainRender;
    this.mainComponents = {
      createNotebook: "createNotebookForm",
      myNotebook: "myNotebookArea",
      coNotebook: "coNotebookArea",
      invitation: "invitationsForm",
      setting: "settingForm",
    }
  }

  async init() {
    this.renderUserInfo();
    this.buttonClickEventRegister();

  }

  async renderUserInfo() {
    //TODO 開發用的url，正式機要移除 domain ;
    const domain = "http://127.0.0.1:8080";
    const path = domain + "/api/user/info";
    const response = await this.requestHandler.sendRequestWithToken(path, "GET", null);
    const data = await response.json();
    $("#username").text(data.username);
    $("#userEmail").text(data.email);
    $("#js_setting_email").text(data.email);
    $("#setting_username").val(data.username);
    localStorage.setItem("email", data.email);
    localStorage.setItem("username", data.username);
  }

  buttonClickEventRegister() {
    $(".settingBtn").on("click", () => this.displaySettingForm(localStorage.getItem("lastMainComponent")));
  }

  displaySettingForm = (lastMainComponent) => {
    if(lastMainComponent !== this.mainComponents.setting) {
      this.notebookMainRender.displayMainComponent(this.mainComponents.setting);
    }
  }
}