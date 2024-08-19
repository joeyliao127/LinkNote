const $ = require("jquery");
import {RequestHandler} from "@unityJS/RequestHandler";

export class SideBarNotebookBtnFactory {
  offset;
  limit;
  requestHandler;
  constructor() {
    this.offset = 0;
    this.limit = 20;
    this.requestHandler = new RequestHandler();
  }

  renderOwnerNotebookBtn = () => {
    const ownerNotebookBtn = $(".js_sideBar_ownerNotebookBtn_Ctn");
    ownerNotebookBtn.clear();
    const ownerBtnList = this.generateOwnerNotebookBtn();
    ownerBtnList.forEach((btn) => {
      ownerNotebookBtn.append(btn);
    })
  }
  generateOwnerNotebookBtn = async () => {
    const path = `/api/notebooks?offset=${this.offset}&limit=${this.limit}`;
    const response = await this.requestHandler.sendRequestWithToken(path, "GET", null);
    if (response.ok) {
      const notebooksData = await response.json();
    } else {

    }
    return [];
  }
}