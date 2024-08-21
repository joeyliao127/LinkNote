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

  renderNotebookBtn = async (params) => {
    params.container.empty();
    const ownerBtnList = await this.generateNotebookBtnList(params.type, params.path, params.offset, params.limit);

    ownerBtnList.forEach((btn) => {
      params.container.append(btn);
    })
  }

  // 產生sidebar的my notebook底下所有notebook btn
  generateNotebookBtnList = async (type, path, offset, limit) => {
    path += `?offset=${offset}&limit=${limit}`;
    const response = await this.requestHandler.sendRequestWithToken(path, "GET", null);

    if (!response.ok) {
      this.messageSender.error("Get notebooks failed");
    }

    let notebookBtnList = [];
    const data = await response.json();
    const notebooks = data.notebooks;
    notebooks.forEach((notebook) => {
      notebookBtnList.push(this.generateNotebookBtn(notebook, type));
    })
    return notebookBtnList;
  }
  generateNotebookBtn = (notebook, type) => {
    const notebookBtn = $(`<p data-description="${notebook.description} data-id="${notebook.id}">${notebook.name}</p>`);
    notebookBtn.addClass(type === "owner" ? "js_sideBar_owner_notebook_btn" : "js_sideBar_collaborative_notebook_btn");
    notebookBtn.on('click', () => {
      switch (type) {
        case "owner":
          renderOwnerNotebookSelectBtn(notebookBtn);
          this.notebookMainRender.displayMainComponent({
            displayComponentName: "specificOwnerNotebook",
            path: `/api/notebooks/${notebook.id}/notes`,
          });
          break;
        case "collaborator":
          renderCollaborativeNotebookSelectBtn(notebookBtn);
          this.notebookMainRender.displayMainComponent({
            displayComponentName: "specificCollaboratorNotebook",
            path: `/api/notebooks/${notebook.id}/notes`,
          });
          break;
      }
    })

    return notebookBtn;
  }
}

function renderOwnerNotebookSelectBtn(selectBtn) {
 $('.js_sideBar_owner_notebook_btn').removeClass("selected");
  selectBtn.addClass("selected");
}

function renderCollaborativeNotebookSelectBtn(selectBtn) {
  $('.js_sideBar_collaborative_notebook_btn').removeClass("selected");
  selectBtn.addClass("selected");
}