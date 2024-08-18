const $ = require( "jquery" );

export class SideBarRender {
  requestHandler;
  mainComponents;
  notebookMainRender;
  constructor(requestHandler, notebookComponentGenerator, notebookMainRender) {
    this.requestHandler = requestHandler;
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
    $(".js_create_notebook_btn").on('click', this.displayCreateNotebookForm);
    $(".settingBtn").on("click", this.displaySettingForm);
    $(".signoutBtn").on("click", this.signOutBtnListener);
    $(".js_invitations_btn").on("click", this.displayInvitationForm);
  }

  // 顯示新增筆記本表單
  displayCreateNotebookForm = () => {
    if(!this.isCurrentMainComponent(this.mainComponents.createNotebook)) {
      this.notebookMainRender.displayMainComponent(this.mainComponents.createNotebook);
    }
  }

  //顯示邀請表單
  displayInvitationForm = () => {
    if(!this.isCurrentMainComponent(this.mainComponents.invitation)) {
      this.notebookMainRender.displayMainComponent(this.mainComponents.invitation);
      this.notebookMainRender.renderInvitationForm();
    }
  }
  //顯示設定表單
  displaySettingForm = () => {
    if(!this.isCurrentMainComponent(this.mainComponents.setting)) {
      this.notebookMainRender.displayMainComponent(this.mainComponents.setting);
      localStorage.setItem("lastMainComponent", this.mainComponents.setting);
    }
  }

  isCurrentMainComponent(displayComponentName) {
    return displayComponentName === localStorage.getItem("lastMainComponent");
  }

  signOutBtnListener = () => {
    localStorage.clear();
    window.location.href = "/";
  }

}