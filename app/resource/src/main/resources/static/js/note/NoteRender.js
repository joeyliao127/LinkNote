const $ = require( "jquery" );
import {NoteComponentGenerator} from "@noteJS/NoteComponentGenerator";
import {RequestHandler} from "@unityJS/RequestHandler";
import {DeleteAlert} from "@unityJS/DeleteAlert";
import {MessageSender} from "@unityJS/MessageSender";
import Editor from '@toast-ui/editor';
import '@toast-ui/editor/dist/toastui-editor.css';
import '@toast-ui/editor/dist/theme/toastui-editor-dark.css';
import 'prismjs/themes/prism.css';
import '@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight.css';
import codeSyntaxHighlight from '@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight-all';
import {CollaborationWebSocket} from "@webSocketJS/CollaborationWebSocket";

export class NoteRender {

  requestHandler = new RequestHandler();
  deleteAlert = new DeleteAlert();
  messageSender = new MessageSender();
  collaborationWebSocket;

  constructor() {
    this.noteId = $(location).attr('href').split("/").pop();
    this.notebookId = $(location).attr('href').split("/")[4];
    this.componentGenerator = new NoteComponentGenerator(this.notebookId, this.noteId);
    this.collaborationWebSocket = new CollaborationWebSocket(this.noteId, localStorage.getItem("username"), localStorage.getItem("email"));
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
    this.renderNoteContent();
    this.renderNoteTags();
    this.connectToCollaborationBroker();
  }

  renderNotebookTags = async () => {
    const tagsComponents = await this.componentGenerator.generateTagsComponent();
    tagsComponents.forEach((tagComponent) => {
      $(".js_tag_area").append(tagComponent);
    })
    this.displaySpecificTagNotesClickEvent();
  }

  renderNoteTags = async () => {
    const noteTags = await this.getNoteTags();
    const data = await this.componentGenerator.getNotebookTags();
    const notebookTags = data.tags;
    if(!(notebookTags.length === 0)) {
      $(`.js_none_tag`).hide();
      const tagsComponents = this.componentGenerator.generateNoteTagsComponent(notebookTags, noteTags);
      tagsComponents.forEach((tagComponent) => {
        $(`.js_note_tag_ctn`).append(tagComponent);
      })
    }
  }

  getNoteTags = async () => {
    const noteTagResponse = await this.requestHandler.sendRequestWithToken(
        `/api/notebooks/${this.notebookId}/notes/${this.noteId}/tags`,
        "GET",
        null
    );

    if(!noteTagResponse.ok) {
      this.messageSender.error("Get tags failed");
    }

    const data = await noteTagResponse.json();
    const {tags} = data;
    return tags;
  }

  renderCollaborators = async () => {
    const response = await this.requestHandler.sendRequestWithToken(
        `/api/notebooks/${this.notebookId}/collaborators`,
        "GET",
        null
    );
    const data = await response.json();
    const {ownerName} = data.owner;
    $(`.js_owner_name`).text(ownerName);

    const collaborators = data.collaborators;
    collaborators.forEach((collaborator) => {
      const collaboratorComponent = this.componentGenerator.generateCollaboratorComponent(collaborator);
      $(".js_collaborator_ctn").append(collaboratorComponent);
    })
  }

  renderNotes = async () => {
    $('.noteArea').empty();
    const notes = await this.componentGenerator.generateNotesComponent();
    notes.forEach((noteComponent) => {
      if(noteComponent.data("noteid") === this.noteId) {
        noteComponent.addClass("selected");
      } else {
        noteComponent.removeClass("selected");
      }
      $('.noteArea').append(noteComponent);
    })
  }

  renderNoteContent =async () => {
    const response = await this.requestHandler.sendRequestWithToken(
        `/api/notebooks/${this.notebookId}/notes/${this.noteId}`,
        "GET",
        null
    )
    const data = await response.json();
    const {note} = data;
    $(`.js_note_name`).text(note.name);
    this.renderTuiEditor(note);
  }

  //todo 渲染tui區域
  renderTuiEditor = (note) => {
    const initialValue = note.content.trim() === "" ? `# Title\n\n## Question\n\n## Keypoint` : note.content;
    const editor = new Editor({
      el: document.querySelector("#editor"),
      height: "93vh",
      previewStyle: "vertical",
      initialValue: initialValue,
      theme: "dark",
      plugins: [codeSyntaxHighlight],
    });
  }

  registerEvent = () => {
    this.displaySideBarClickEvent();
    this.displayTagsClickEvent();
    this.displayCollaboratorsClickEvent();
    this.displayFilterClickEvent();
    this.displayFilterTagClickEvent();
    this.createNoteClickEvent();
    this.signOutClickEvent();
    this.navigateToNotebookClickEvent();
    this.clickBodyEvent();
    this.sideBarClickEvent();
    this.createTagEvent();
    this.registerFiltersEvent();
    this.deleteNoteClickEvent();
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
    $(".js_create_note_btn").on("click", async (e) => {
      e.stopPropagation();
      try{
        const response = await this.requestHandler.sendRequestWithToken(
            `/api/notebooks/${this.notebookId}/notes`,
            "POST",
            {notebookId: this.notebookId}
        );
        if(response.ok) {
          const data = await response.json();
          window.location.href = `/notebooks/${this.notebookId}/notes/${data.noteId}`
        }
      }
      catch(err) {
        this.messageSender.error("create note error");
      }
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
    const tagComponent = this.componentGenerator.generateTagComponent(tag)
    $(".js_tag_area").append(tagComponent);
    tagComponent.on("click", async (e) => {
      this.componentGenerator.filters.tag = tagComponent.text();
      await this.renderNotes();
      this.renderSelectedTagFilter(tagComponent.get(0));
    })
    input.val("");
  }

  registerFiltersEvent = () => {
    this.displayAllNotesClickEvent();
    this.displayStarredNotesClickEvent();
    this.displaySortedNotesClickEvent();
    this.displaySearchNotesClickEvent();
  }

  displaySearchNotesClickEvent = () => {
    const input = $(".js_search_note_input");
    input.on("keypress", async (e) => {
      if(e.key === "Enter") {
        this.componentGenerator.filters.keyword = input.val();
        await this.renderNotes();
        input.val("");
      }
    })

    //監聽input click事件避免點擊後收起side bar (body有監聽close事件)
    input.on('click', (e) => {
      e.stopPropagation();
    })

    $('.js_search_note_btn').on("click", async (e) => {
      e.stopPropagation();
      this.componentGenerator.filters.keyword = input.val();
      await this.renderNotes();
      input.val("");
    })
  }

  displayAllNotesClickEvent = () => {
    $(".js_all_note_btn").on("click", async () => {
      this.componentGenerator.resetFilters();
      this.renderSelectedFilter($(`.js_all_note_btn`));
      await this.renderNotes();
    });
  }

  displayStarredNotesClickEvent = () => {
    $(".js_star_note_btn").on("click", async () => {
      this.componentGenerator.filters.star = !this.componentGenerator.filters.star;
      $(`.js_star_note_btn`).find(".filterCheck").toggleClass("display-none");
      await this.renderNotes();
    });
  }

  displaySortedNotesClickEvent = () => {
    $(`.js_sort_note_btn`).on("click", async () => {
      this.componentGenerator.filters.sortByDesc = !this.componentGenerator.filters.sortByDesc;
      $(`.js_sort_note_btn`).find(".filterCheck").toggleClass("display-none");
      await this.renderNotes();
    })
  }

  displaySpecificTagNotesClickEvent = () => {
    const tags = document.querySelectorAll(".js_specific_tag");
    tags.forEach((tag) => {
      tag.addEventListener("click", async () => {
        this.componentGenerator.filters.tag = tag.querySelector(".js_tag_name").textContent;
        await this.renderNotes();
        this.renderSelectedTagFilter(tag);
      })
    })
  }

  renderSelectedFilter = (target) => {
    $(".filterBtn").find('.filterCheck').addClass("display-none");
    target.find('.filterCheck').removeClass("display-none");
  }
  renderSelectedTagFilter = (target) => {
    $('.js_specific_tag').removeClass("selected");
    target.classList.add("selected");
  }

  deleteNoteClickEvent = () => {
    $(`.js_delete_note_btn`).on('click', async () => {
      this.deleteAlert.renderDeleteAlertBox(
          "",
          "this note",
          async () => {
            const path = `/api/notebooks/${this.notebookId}/notes/${this.noteId}`;
            const response = await this.requestHandler.sendRequestWithToken(path, "DELETE", null);
            if(!response.ok) {
              this.messageSender.error("Delete note failed");
              return;
            }
            window.location.href = `/notebooks`;
          }
      )
    })
  }
  connectToCollaborationBroker = () => {
    this.collaborationWebSocket.connect();
  }


}