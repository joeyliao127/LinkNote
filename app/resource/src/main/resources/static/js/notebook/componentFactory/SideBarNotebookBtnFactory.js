const $ = require("jquery");
import {RequestHandler} from "@unityJS/RequestHandler";
import {MessageSender} from "@unityJS/MessageSender";
import {NotebookMainRender} from "@notebookJS/NotebookMainRender";

export class SideBarNotebookBtnFactory {
  offset;
  limit;
  requestHandler;
  messageSender;
  notebookMainRender;
  constructor() {
    this.offset = 0;
    this.limit = 20;
    this.requestHandler = new RequestHandler();
    this.messageSender = new MessageSender();
    this.notebookMainRender = new NotebookMainRender();
  }

  renderOwnerNotebookBtn = async () => {
    $(".js_sideBar_ownerNotebookBtn_Ctn").empty();
    const ownerBtnList = await this.generateOwnerNotebookBtnList();

    ownerBtnList.forEach((btn) => {
      $(".js_sideBar_ownerNotebookBtn_Ctn").append(btn);
    })
  }

  // 產生sidebar的my notebook底下所有notebook btn
  generateOwnerNotebookBtnList = async () => {
    const path = `/api/notebooks?offset=${this.offset}&limit=${this.limit}`;
    const response = await this.requestHandler.sendRequestWithToken(path, "GET", null);

    if (!response.ok) {
      this.messageSender.error("Get notebooks failed");
    }

    let notebookBtnList = [];
    const data = await response.json();
    const notebooks = data.notebooks;
    notebooks.forEach((notebook) => {
      notebookBtnList.push(this.generateOwnerNotebookBtn(notebook));
    })
    return notebookBtnList;
  }
  generateOwnerNotebookBtn = (notebook) => {
    const notebookBtn = $(`<p data-description="${notebook.description} data-id="${notebook.id}">${notebook.name}</p>`);
    notebookBtn.on('click', () => {
      this.notebookMainRender.displayMainComponent("specificOwnerNotebook");
    })
  }


}