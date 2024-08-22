import {MessageSender} from "@unityJS/MessageSender";
import {DeleteAlert} from "@unityJS/DeleteAlert";
import {RequestHandler} from "@unityJS/RequestHandler";
import {Validator} from "@unityJS/Validator";

const $ = require("jquery");


export class NoteToolBarComponentFactory {
  requestHandler = new RequestHandler();
  messageSender = new MessageSender();
  deleteAlert = new DeleteAlert();
  validator = new Validator();

  allNoteBtn;
  tag;
  tagBtn;
  sortByDesc;
  sortBtn;
  keyword;
  star;
  starBtn;
  filters;
  noteCardCtn;

  constructor(notebook, noteCtn, genNoteCard) {
    this.noteCtn = noteCtn;
    this.genNoteCard = genNoteCard;
    this.filters = {
      tag: "",
      sortByDesc: false,
      star: false,
      keyword: null,
    };
    this.notebookId = notebook.id;
    this.notebookName = notebook.name;
  }

  genCreateNoteComponent = () => {
    const createNoteBtn = $(`
      <div class="createNoteBtn js_create_note_btn">
          <p>New note</p>
      </div>
    `);

    createNoteBtn.on("click", async () => {
      const path = `/api/notebooks/${this.notebookId}/notes`;
      const response = await this.requestHandler.sendRequestWithToken(path, "POST", {
        notebookId: this.notebookId,
      });
      if (response.ok) {
        const data = await response.json();
        window.location.href = `/notebooks/${this.notebookId}/notes/${data.noteId}`;
      } else {
        MessageMaker.failed("Create note failed");
      }
    });

    return createNoteBtn;
  }

  genInvitationBtnComponent = () => {
    const inviteBtn = $(`
      <div class="toolBtn js_invite_btn">
        <img src="/icons/addCollaborator.png" alt="collaboratorBtn"/>
      </div> 
    `);
    inviteBtn.on('click', () => {
      $('.collaboratorForm').toggleClass("display-none");
      $('.tagForm').addClass('display-none');
    })
    return inviteBtn;
  }

  genAllNoteComponent = () => {
    const btn = $(`
      <div class="toolBtn js_all_note_btn">
        <img src="/icons/box.png" alt="box"/>
      </div>
    `);

    btn.on('click',async () => {
      this.closeForm();
      this.resetFilter();
      await this.rerenderNoteCardCtn();
    });

    this.allNoteBtn = btn;
    return btn;
  }

  genTagComponent = () => {
    const btn = $(`
      <div class="toolBtn js_tag_btn">
        <img src="/icons/tag.png" alt="tag"/>
      </div>
    `);

    btn.on("click", () => {
      $('.tagForm').toggleClass("display-none");
      $('.collaboratorForm').addClass('display-none');
    })
    this.tagBtn = btn;
    return btn;
  }

  genSortComponent = () => {
    const btn = $(`
       <div class="toolBtn js_sort_btn">
        <img src="/icons/clock.png" alt="clock"/>
      </div>
    `);

    btn.on("click",async () => {
      this.filters.sortByDesc = !this.filters.sortByDesc;
      this.renderSelectedFilter();
      await this.rerenderNoteCardCtn();
    });

    this.sortBtn = btn;
    return btn;
  }

  genStarComponent = () => {
    const btn = $(`
      <div class="toolBtn js_star_btn">
        <img src="/icons/full-star.png" alt="star"/>
      </div>
    `);

    btn.on("click",async () => {
      this.filters.star = !this.filters.star;
      this.renderSelectedFilter();
      await this.rerenderNoteCardCtn();
    });

    this.starBtn = btn;
    return btn;
  }

  genSearchComponent = () => {
    const searchBar = $(`
      <div class="searchNote js_search_btn search">
        <input type="text" class="js_search_notes_input" id="searchNote" placeholder="search notes"/>
        <img src="/icons/search.png" alt="search"/>
      </div>
    `);

    searchBar.find('.js_search_notes_input').on('keypress', async (e) => {
      if (e.key === "Enter") {
        this.filters.keyword = searchBar.find('input').val();
        searchBar.find('input').val("");
        await this.rerenderNoteCardCtn();
      }
    });

    searchBar.find('img').on('click',async () => {
      this.filters.keyword = searchBar.find('input').val();
      searchBar.find('input').val("");
      await this.rerenderNoteCardCtn();
    });

    return searchBar;
  }

  genDeleteNotebookComponent = () => {
    const deleteBtn = $(`
      <div class="toolBtn js_delete_notebook_btn">
          <img src="/icons/trash-white.png" alt="deleteBtn" class="deleteBtn"/>
      </div>
    `);

    deleteBtn.on('click', () => {
      this.deleteAlert.renderDeleteAlertBox("Notebook", this.notebookName, async () => {
        const path = `/api/notebooks/${this.notebookId}`;
        const response = await this.requestHandler.sendRequestWithToken(path, "DELETE", null);
        if (response.ok) {
          window.location.reload();
        } else {
          this.messageSender.error("Delete notebook failed");
        }
      });
    });
    return deleteBtn;
  }

  genTagFilterForm = async () => {
    const tagForm = $(`
      <div class="tagForm display-none">
        <h5>Tags</h5>
        <div class="tagCtn">
        </div>
        <div class="createTagForm">
        <input type="text" class="createTagInput js_create_tag_input" placeholder="Create new tag"/>
        <button id="createTagBtn" class="js_create_tag_btn">Create</button>
      </div>
    `);
    const response = await this.requestHandler.sendRequestWithToken(`/api/notebooks/${this.notebookId}/tags`, "GET", null);

    if (!response.ok) {
      this.messageSender.error("Get tags failed");
    }

    const data = await response.json();
    data.tags.forEach((tag) => {
      tagForm.find(".tagCtn").append(this.genTagBtn(tag));
    });

    tagForm.find(".js_create_tag_btn").on("click", async () => {
      const input = tagForm.find(".js_create_tag_input");
      const tagName = input.val();

      if( tagName.trim() === "") {
        this.messageSender.warning("Tag name cannot be empty");
        return;
      }
      const response = await this.createTag(tagName);

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
      tagForm.find(".tagCtn").append(this.genTagBtn(tag));
      input.val("");
    });

    tagForm.find(".js_create_tag_input").on("keypress",async (e) => {
      if (e.key === "Enter") {
        const input = tagForm.find(".createTagInput");
        const tagName = input.val();

        if( tagName.trim() === "") {
          this.messageSender.warning("Tag name cannot be empty");
          return;
        }
        const response = await this.createTag(tagName);

        if (!response.ok) {
          this.messageSender.error("Create tag failed");
          return;
        }

        const data = response.json();
        const tag = {
          name: tagName,
          tagId: data.tagId
        };
        tagForm.find(".tagCtn").append(this.genTagBtn(tag));
        input.val("");
      }
    });

    return tagForm;
  }

  genTagBtn = (tag) => {
    const tagEl = $(`
      <div class="tag js_tag_filter_el">
        <p>${tag.name}</p>
        <div class="deleteTagBtn">
          <p>Remove</p>
        </div>
      </div>
    `);

    //刪除tag
    tagEl.find('.deleteTagBtn').on('click', async (e) => {
      e.stopPropagation();
      const path = `/api/notebooks/${this.notebookId}/tags?tagId=${tag.tagId}`;
      const response = await this.requestHandler.sendRequestWithToken(path, "DELETE");
      if (response.ok) {
        tagEl.remove();
      } else {
        this.messageSender.error(`Delete tag ${tag.name} failed.`);
      }
    })

    //選擇tag
    tagEl.on('click',async () => {
      this.filters.tag = tag.name;
      tagEl.parent().parent().addClass("display-none");
      this.renderSelectedTag(tagEl);
      await this.rerenderNoteCardCtn();
    })

    return tagEl;
  }

  renderSelectedTag(target) {
    $(`.js_tag_filter_el`).removeClass("selected");
    target.addClass("selected");
  }

  createTag = async (tagName) => {
    const path = `/api/notebooks/${this.notebookId}/tags`;
    const requestBody = {
      name: tagName
    };
    return await this.requestHandler.sendRequestWithToken(path, "POST", requestBody);
  }
  genInvitationForm = async () => {
    const invitationForm = $(`
      <div class="collaboratorForm display-none">
        <h5>Collaborators</h5>
        <div class="collaborators"></div>
        <div class="invitationForm">
        <p>Invite Collaborator</p>
        <input type="email" id="inviteEmail" placeholder="Email Address"/>
        <input type="text" id="inviteMessage" placeholder="Invitation message"/>
        <div id="inviteBtn" class="js_send_invitation_btn">Invite</div>
      </div>
      </div>
    `);

    invitationForm.find(`.js_send_invitation_btn`).on("click", async () => {
      const email = invitationForm.find("#inviteEmail").val();
      const message = invitationForm.find("#inviteMessage").val();
      if(!this.validator.validateEmailFormat(email)) {
        this.messageSender.error("Invalid email format");
      }
      const result = await this.inviteCollaborator(email, message, this.notebookId);

      if(result.ok) {
        this.messageSender.success("Invitation sent");
        return;
      } else {
        this.messageSender.error("Invitation failed");
      }
      invitationForm.find("#inviteEmail").val("");
      invitationForm.find("#inviteMessage").val("");
      invitationForm.addClass("display-none");
    });

    const response = await this.requestHandler.sendRequestWithToken(`/api/notebooks/${this.notebookId}/collaborators`, "GET");

    if (!response.ok) {
      this.messageSender.error("Get invitations failed");
    }
    const data = await response.json();

    if(data.collaborators.length === 0) {
      return invitationForm;
    }

    data.collaborators.forEach((collaborator) => {
      invitationForm.find(".collaborators").append(this.genInvitationBtn(collaborator));
    });

    return invitationForm;
  }

  inviteCollaborator = async (email, message) => {
    const path = `/api/notebooks/${this.notebookId}/invitations`;
    const requestBody = {
      inviteeEmail: email,
      message
    };
    return await this.requestHandler.sendRequestWithToken(path, "POST", requestBody);
  }

  genInvitationBtn = (collaborator) => {
    const collaboratorEl = $(`
      <div class="collaborator">
        <p>${collaborator.name}</p>
        <div class=deleteCollaboratorBtn data-userEmail= ${collaborator.userEmail}>
          <p>Remove</p>
        </div>
      </div>
    `);

    collaboratorEl.on("click", () => {
      this.deleteAlert.renderDeleteAlertBox(
          "Note",
          collaborator.name,
          async () => {
            await this.deleteCollaborator(collaborator);
            collaboratorEl.remove();
          }
      );
    });

    return collaboratorEl;
  }

  async deleteCollaborator(collaborator) {
    const path = `api/notebooks/${notebookId}/collaborators?userEmail=${collaborator.userEmail}`;
    const response = await this.requestHandler.sendRequestWithToken(path, "DELETE");
    if (response.ok) {
      this.messageSender.success("Collaborator removed.");
    } else {
      this.messageSender.error("Error");
    }
  }
  generateFilterPath() {
    let filterPath = `/api/notebooks/${this.notebookId}/notes?offset=0&limit=20`;
    if (this.filters.tag.trim() !== "") {
      filterPath += `&tag=${this.filters.tag}`;
    }
    if (this.filters.star) {
      filterPath += "&star=true";
    } else {
      filterPath += "&star=false";
    }
    if (this.filters.sortByDesc) {
      filterPath += "&sortDesc=true";
    } else {
      filterPath += "&sortDesc=false";
    }

    if (this.filters.keyword) {
      filterPath += `&keyword=${this.filters.keyword}`;
    }

    return filterPath;
  }

  resetFilter() {
    this.filters.tag = "";
    this.filters.sortByDesc = false;
    this.filters.star = false;
    this.filters.keyword = null;
    this.renderSelectedFilter();
  }


  renderSelectedFilter(){

    let selectedAllBtn = true;

    if (this.filters.tag.length === 0) {
      this.tagBtn.removeClass("selected");
    } else {
      this.tagBtn.addClass("selected");
      selectedAllBtn = false;
    }

    if(this.filters.star) {
      this.starBtn.addClass("selected");
      selectedAllBtn = false;
    } else {
      this.starBtn.removeClass("selected");
    }

    if(this.filters.sortByDesc) {
      this.sortBtn.addClass("selected");
      selectedAllBtn = false;
    } else {
      this.sortBtn.removeClass("selected");
    }

    if(selectedAllBtn) {
      this.allNoteBtn.addClass("selected");
    } else {
      this.allNoteBtn.removeClass("selected");
    }
  }

  closeForm = () => {
    $('.tagForm').addClass("display-none");
    $('.collaboratorForm').addClass('display-none');
  }

  rerenderNoteCardCtn = async () => {
    const path = this.generateFilterPath();
    const response = await this.requestHandler.sendRequestWithToken(path, "GET");
    if (response.ok) {
      const data = await response.json();
      this.noteCtn.find(".noteCardCtn").empty();
      data.notes.forEach((note) => {
        this.noteCtn.find(".noteCardCtn").append(this.genNoteCard(note, this.notebookId));
      })
    }
  }
}