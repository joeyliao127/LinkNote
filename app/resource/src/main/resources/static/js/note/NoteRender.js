const $ = require( "jquery" );
import {NoteComponentGenerator} from "@noteJS/NoteComponentGenerator";
import {RequestHandler} from "@unityJS/RequestHandler";

export class NoteRender {

  componentGenerator = new NoteComponentGenerator();
  requestHandler = new RequestHandler();

  constructor() {
    this.noteId = $(location).attr('href').split("/").pop();
    this.notebookId = $(location).attr('href').split("/")[4];
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

  renderMain = () => {
    this.renderTags();
    this.renderCollaborators();
    this.renderNoteContent();
  }

  renderTags = () => {

  }

  renderCollaborators = () => {

  }

  renderNotes = async () => {

  }

  renderNoteContent = async () => {

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

  clickBodyEvent = () => {
    $("body").on("click", () => {
      this.closeForm("body");
    })
  }
}