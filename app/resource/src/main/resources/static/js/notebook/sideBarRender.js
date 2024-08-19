const $ = require( "jquery" );
import {RequestHandler} from "@unityJS/RequestHandler";
import {SideBarNotebookBtnFactory} from "@notebookJS/componentFactory/SideBarNotebookBtnFactory";

export class SideBarRender {
  requestHandler;
  mainComponents;
  notebookMainRender;
  sideBarNotebookBtnFactory;
  sideBarBtnList;
  constructor(notebookMainRender) {
    this.requestHandler = new RequestHandler();
    this.sideBarNotebookBtnFactory = new SideBarNotebookBtnFactory();
    this.notebookMainRender = notebookMainRender;
    this.mainComponents = {
      createNotebook: "createNotebookForm",
      myNotebook: "myNotebookArea",
      coNotebook: "coNotebookArea",
      invitation: "invitationsForm",
      setting: "settingForm",
    }

    this.sideBarBtnList = {
      "createNotebook": $('.js_create_notebook_btn'),
      "myNotebook": $('.js_sideBar_myNotebook_btn'),
      "coNotebook": $('.js_sideBar_coNotebook_btn'),
      "invitation": $('.js_invitations_btn'),
      "setting": $('.js_setting_btn'),
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
    $(".js_sideBar_myNotebook_btn").on('click', this.displayMyNotebooks);
    $(".js_sideBar_coNotebook_btn").on('click', this.displayCoNotebooks);
    $(".settingBtn").on("click", this.displaySettingForm);
    $(".signoutBtn").on("click", this.signOutBtnListener);
    $(".js_invitations_btn").on("click", this.displayInvitationForm);
  }

  // 顯示新增筆記本表單
  displayCreateNotebookForm = () => {
    if(!this.isCurrentMainComponent(this.mainComponents.createNotebook)) {
      this.renderSelectedSideBarBtn("createNotebook");
      this.notebookMainRender.displayMainComponent(this.mainComponents.createNotebook);
      localStorage.setItem("lastMainComponent", this.mainComponents.createNotebook);
    }
  }

  displayMyNotebooks = () => {
    this.sideBarNotebookBtnFactory.renderOwnerNotebookBtn()
    if(!this.isCurrentMainComponent(this.mainComponents.myNotebook)) {
      this.renderSelectedSideBarBtn("myNotebook");
      this.notebookMainRender.displayMainComponent(this.mainComponents.myNotebook);
      localStorage.setItem("lastMainComponent", this.mainComponents.myNotebook);
    }
  }

  displayCoNotebooks = () => {
    if(!this.isCurrentMainComponent(this.mainComponents.coNotebook)) {
      this.renderSelectedSideBarBtn("coNotebook");
      this.notebookMainRender.displayMainComponent(this.mainComponents.coNotebook);
      localStorage.setItem("lastMainComponent", this.mainComponents.coNotebook);
    }
  }

  //顯示邀請表單
  displayInvitationForm = () => {
    if(!this.isCurrentMainComponent(this.mainComponents.invitation)) {
      this.renderSelectedSideBarBtn("invitation");
      this.notebookMainRender.displayMainComponent(this.mainComponents.invitation);
      localStorage.setItem("lastMainComponent", this.mainComponents.invitation);
      // this.notebookMainRender.renderInvitationForm();
    }
  }

  //顯示設定表單
  displaySettingForm = () => {
    if(!this.isCurrentMainComponent(this.mainComponents.setting)) {
      this.renderSelectedSideBarBtn("setting");
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

  renderSelectedSideBarBtn(btnName) {
    $('.js_sideBar_ownerNotebookBtn_Ctn').empty();
    $('.js_sideBar_coNotebookBtn_Ctn').empty();
    Object.entries(this.sideBarBtnList).forEach(([name, btn]) => {
      if (btnName === name) {
        btn.addClass("selected");
      } else {
        btn.removeClass("selected");
      }
    })
  }
}