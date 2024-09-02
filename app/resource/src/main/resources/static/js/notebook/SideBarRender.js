const $ = require( "jquery" );
import {RequestHandler} from "@unityJS/RequestHandler";
import {SideBarNotebookBtnFactory} from "@notebookJS/componentFactory/SideBarNotebookBtnFactory";
import {URL} from "@unityJS/URL";

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
    this.URL = new URL();
  }

  async init() {
    this.renderUserInfo();
    this.buttonClickEventRegister();
    this.displayMyNotebooks("/api/notebooks?offset=0&limit=20");
  }

  async renderUserInfo() {
    const path = this.URL.domain + "/api/user/info";
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
    $(".js_create_notebook_btn").on('click', () => {
      if(!this.isCurrentMainComponent(this.mainComponents.createNotebook)) {
        this.displayCreateNotebookForm();
      }
    });
    $(".js_sideBar_myNotebook_btn").on('click', () => {
      if(!this.isCurrentMainComponent(this.mainComponents.myNotebook)) {
        this.displayMyNotebooks("/api/notebooks?offset=0&limit=20");
      }
    });
    $(".js_sideBar_coNotebook_btn").on('click', () => {
      if(!this.isCurrentMainComponent(this.mainComponents.coNotebook)) {
        this.displayCoNotebooks("/api/coNotebooks?offset=0&limit=20");
      }
    });
    $(".settingBtn").on("click",() => {
      if(!this.isCurrentMainComponent(this.mainComponents.setting)) {
        this.displaySettingForm();
      }
    });
    $(".js_invitations_btn").on("click", () => {
      if(!this.isCurrentMainComponent(this.mainComponents.invitation)) {
        this.displayInvitationForm();
      }
    });
    $(".signoutBtn").on("click", this.signOutBtnListener);

  }

  // 顯示新增筆記本表單
  displayCreateNotebookForm = () => {
    this.renderSelectedSideBarBtn("createNotebook");
    this.notebookMainRender.displayMainComponent({
      displayComponentName: this.mainComponents.createNotebook
    });
    localStorage.setItem("lastMainComponent", this.mainComponents.createNotebook);
  }

  displayMyNotebooks = (path) => {
    this.renderSelectedSideBarBtn("myNotebook");
    const config = {
      type: "owner",
      container: $('.js_sideBar_ownerNotebookBtn_Ctn'),
      path: `/api/notebooks`,
      offset: 0,
      limit: 20
    }
    this.sideBarNotebookBtnFactory.renderNotebookBtn(config);
    this.notebookMainRender.displayMainComponent({
      displayComponentName: this.mainComponents.myNotebook,
      path: path
    });
    localStorage.setItem("lastMainComponent", this.mainComponents.myNotebook);
  }

  displayCoNotebooks = (path) => {
    this.renderSelectedSideBarBtn("coNotebook");
    const config = {
      type: "collaborator",
      container: $('.js_sideBar_coNotebookBtn_Ctn'),
      path: `/api/coNotebooks`,
      offset: 0,
      limit: 20
    }
    this.sideBarNotebookBtnFactory.renderNotebookBtn(config);
    this.notebookMainRender.displayMainComponent({
      displayComponentName: this.mainComponents.coNotebook,
      path: path
    });
    localStorage.setItem("lastMainComponent", this.mainComponents.coNotebook);
  }

  //顯示邀請表單
  displayInvitationForm = () => {
    this.renderSelectedSideBarBtn("invitation");
    this.notebookMainRender.displayMainComponent({
      displayComponentName: this.mainComponents.invitation
    });
    localStorage.setItem("lastMainComponent", this.mainComponents.invitation);
  }

  //顯示設定表單
  displaySettingForm = () => {
    this.renderSelectedSideBarBtn("setting");
    this.notebookMainRender.displayMainComponent({
      displayComponentName: this.mainComponents.setting
    });
    localStorage.setItem("lastMainComponent", this.mainComponents.setting);
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