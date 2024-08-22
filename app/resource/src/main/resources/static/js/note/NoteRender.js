const $ = require( "jquery" );
import {NoteComponentGenerator} from "@noteJS/NoteComponentGenerator";
import {RequestHandler} from "@unityJS/RequestHandler";
import {DeleteAlert} from "@unityJS/DeleteAlert";
import {MessageSender} from "@unityJS/MessageSender";

export class NoteRender {

  requestHandler = new RequestHandler();
  deleteAlert = new DeleteAlert();
  messageSender = new MessageSender();

  constructor() {
    this.noteId = $(location).attr('href').split("/").pop();
    this.notebookId = $(location).attr('href').split("/")[4];
    this.componentGenerator = new NoteComponentGenerator(this.notebookId);
  }

  init() {
    this.renderSideBar();
    this.renderMain();
    this.registerEvent();
    this.forms = {
      "collaboratorForm": $(".js_collaborator_ctn"),
      "tagsForm": $(".js_note_tag_form"),
      "filterForm": $(".js_filter_ctn"),
      "notebookTagsForm": $(".js_notebook_tags_ctn"),
    }
  }

  renderSideBar = () => {
    this.renderUserInfo();
    this.renderNotebook();
    this.renderNotebookTags();
    this.renderNotes();
  }

  renderUserInfo = async () => {
    //TODO 開發用的url，正式機要移除 domain ;
    const domain = "http://127.0.0.1:8080";
    const path = domain + "/api/user/info";
    const response = await this.requestHandler.sendRequestWithToken(path, "GET", null);
    const data = await response.json();
    $(".js_user_email").text(data.email);
  }

  renderNotebook = async () => {
    const response = await this.requestHandler.sendRequestWithToken(
        `/api/notebooks/${this.notebookId}`,
        "GET",
        null
    );

    if(response.ok) {
      const data = await response.json();
      $(".notebookName").text(data.name);
    }
  }

  renderMain = async () => {
    this.renderCollaborators();
    const response = await this.requestHandler.sendRequestWithToken(
        `/api/notebooks/${this.notebookId}/notes/${this.noteId}`,
        "GET",
        null
    )
    const data = await response.json();
    this.renderNoteContent(data.note);
    this.renderTags(data.tags);
  }

  renderNotebookTags = async () => {
    const tagsComponents = await this.componentGenerator.generateTagsComponent();
    tagsComponents.forEach((tagComponent) => {
      $(".js_tag_area").append(tagComponent);
    })
  }

  renderTags = (tags) => {
    //todo 渲染tags元件
  }

  renderCollaborators = async () => {
    //todo 渲染collaborator元件
  }

  renderNotes = async () => {
    const notes = await this.componentGenerator.generateNotesComponent();
    notes.forEach((noteComponent) => {
      $('.noteArea').append(noteComponent);
    })
  }

  renderNoteContent = (note) => {
    //todo 渲染tui editor，將內容和標題插入html
  }
  registerEvent = () => {
    this.displaySideBarClickEvent();
    this.displayTagsClickEvent();
    this.displayCollaboratorsClickEvent();
    this.displayFilterClickEvent();
    this.displayFilterTagClickEvent();
    this.createNoteClickEvent();
    this.searchNoteEvent();
    this.signOutClickEvent();
    this.navigateToNotebookClickEvent();
    this.clickBodyEvent();
    this.sideBarClickEvent();
    this.createTagEvent();
  }

  closeForm = (excludeForm) => {
    Object.entries(this.forms).forEach((key) => {
      if(key[0] !== excludeForm) {
        key[1].addClass("display-none");
      }
    })
  }

  displaySideBarClickEvent = () => {
    $(".js_hidden_sideBar_btn").on('click', (e) => {
      e.stopPropagation();
      $('.sideBar').removeClass("displaySideBar");
      this.closeForm($(`.sideBar`));
    })

    $(".js_display_sidebar_btn").on("click", (e) => {
      e.stopPropagation();
      $('.sideBar').addClass("displaySideBar");
    })
  }

  displayTagsClickEvent = () => {
    $(".js_note_tags_btn").on('click', (e) => {
      e.stopPropagation();
      $(".js_note_tag_form").toggleClass("display-none");
      this.closeForm("tagsForm");
    })
  }
  displayCollaboratorsClickEvent = () => {
    $(".js_collaborator_btn").on('click', (e) => {
      e.stopPropagation();
      $(".js_collaborator_ctn").toggleClass("display-none");
      this.closeForm("collaboratorForm");
    })
  }
  displayFilterClickEvent = () => {
    $(".js_filter_btn").on('click', (e) => {
      e.stopPropagation();
      $(".js_filter_ctn").toggleClass("display-none");
      this.closeForm("filterForm");
    })
  }
  displayFilterTagClickEvent = () => {
    $(".js_notebook_tags_btn").on('click', (e) => {
      e.stopPropagation();
      $(".js_notebook_tags_ctn").toggleClass("display-none");
      this.closeForm("notebookTagsForm");
    })
  }
  createNoteClickEvent = () => {

  }
  searchNoteEvent = () => {
    const input = $(".js_search_note_input");
    input.on("keypress", (e) => {
      if(e.key === "Enter") {
        const keyword = $(".js_search_note_input").val();
        this.displayMainComponent({
          displayComponentName: "noteList",
          path: `/api/notebooks/${this.notebookId}/notes?keyword=${keyword}&offset=0&limit=20`
        });
      }
    })

    //監聽input click事件避免點擊後收起side bar (body有監聽close事件)
    input.on('click', (e) => {
      e.stopPropagation();
    })

    $('.js_search_note_btn').on("click", (e) => {
      e.stopPropagation();
    })



  }
  signOutClickEvent = () => {
    $(".js_signOut_btn").on("click", () => {
      localStorage.clear();
      window.location.href = "/";
    })
  }
  navigateToNotebookClickEvent = () => {
    $(".js_notebook_navigate_btn").on("click", () => {
      window.location.href = `/notebooks/${this.notebookId}`;
    })
  }

  sideBarClickEvent = () => {
    $(".sideBar").on("click", (e) => {
      e.stopPropagation();
    })
  }

  clickBodyEvent = () => {
    $("body").on("click", () => {
      this.closeForm("body");
      $('.sideBar').removeClass("displaySideBar");
    })
  }

  createTagEvent = () => {
    $(".js_create_tag_btn").on("click", async () => {
      await this.createTag();
    });

    $(".js_create_tag_input").on("keypress",async (e) => {
      if (e.key === "Enter") {
        await this.createTag();
      }
    });
  }

  createTag = async () => {
    const input = $(".js_create_tag_input");
    const tagName = input.val();

    if( tagName.trim() === "") {
      this.messageSender.warning("Tag name cannot be empty");
      return;
    }
    const path = `/api/notebooks/${this.notebookId}/tags`;
    const requestBody = {
      name: tagName
    };
    const response = await this.requestHandler.sendRequestWithToken(path, "POST", requestBody);

    if (response.status === 400) {
      this.messageSender.error("Tag name already exists");
      return;
    } else if(!response.ok) {
      this.messageSender.error("Create tag failed");
      return;
    }

    const data = await response.json();
    const tag = {
      name: tagName,
      tagId: data.tagId
    };
    $(".js_tag_area").append(this.componentGenerator.generateTagComponent(tag));
    input.val("");
  }
}