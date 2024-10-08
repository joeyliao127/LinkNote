import {MessageSender} from "@unityJS/MessageSender";
import {RequestHandler} from "@unityJS/RequestHandler";
import {DeleteAlert} from "@unityJS/DeleteAlert";
const $ = require("jquery");

export class NoteComponentGenerator {

  messageSender = new MessageSender();
  requestHandler = new RequestHandler();
  deleteAlert = new DeleteAlert();

  constructor(notebookId, noteId) {
    this.notebookId = notebookId;
    this.noteId = noteId;
    this.filters = {
      "star": false,
      "tag": "",
      "keyword": "",
      "sortByDesc": false
    }
  }

  getNotes = async () => {
    let notes = [];
    let hasNextPage = true;

    do{
      const response = await this.requestHandler.sendRequestWithToken(this.getPath(), "GET", null);

      if(!response.ok) {
        this.messageSender.error("Get notes failed");
      }

      const data = await response.json();
      notes.push(...data.notes);
      hasNextPage = data.nextPage;
    } while (hasNextPage);

    return notes;
  }

  generateNotesComponent = async () => {
    const notes = await this.getNotes();
    let notesComponent = [];
    notes.forEach((note) => {
      notesComponent.push(this.generateNoteComponent(note));
    })

    return notesComponent;
  }

  generateNoteComponent = (note) => {
    const noteComponent = $(`
      <a class="note" data-noteid="${note.noteId}">
        <p>${note.name}</p>
        <div class="star ${note.star ? "star-full" : "star-empty"}"></div>
      </a>
    `);


    noteComponent.find(".star").on("click", async (e) => {
      e.stopPropagation();
      const starBtn = noteComponent.find(".star");
      let requestBody = {
        name: note.name,
        star: false,
      };

      if(starBtn.hasClass("star-full")) {
        starBtn.removeClass("star-full");
        starBtn.addClass("star-empty");
        requestBody.star = false;
      } else {
        starBtn.removeClass("star-empty");
        starBtn.addClass("star-full");
        requestBody.star = true;
      }
      await this.updateNoteStar(requestBody, note.noteId);
    })

    noteComponent.on("click", () => {
      window.location.href = `/notebooks/${this.notebookId}/notes/${note.noteId}`
    })

    return noteComponent;
  }

  getPath = () => {
    let path = `/api/notebooks/${this.notebookId}/notes?offset=0&limit=20`;
    if(this.filters.star) {
      path += "&star=true";
    }
    if(this.filters.tag.trim() !== "") {
      path += `&tag=${this.filters.tag}`;
    }
    if(this.filters.sortByDesc) {
      path += "&sortDesc=true";
    }
    if(this.filters.keyword.trim() !== "") {
      path += `&keyword=${this.filters.keyword}`;
    }

    return path;
  }

  updateNoteStar = async (requestBody, noteId) => {
    const path = `/api/notebooks/${this.notebookId}/notes/${noteId}`;
    const response = await this.requestHandler.sendRequestWithToken(path, "PUT", requestBody);
    if(!response.ok) {
      this.messageSender.error("Update note star failed");
    }
  }

  generateTagsComponent = async () => {
    const data = await this.getNotebookTags();
    let tagsComponent = [];
    data.tags.forEach((tag) => {
      tagsComponent.push(this.generateTagComponent(tag));
    })

    return tagsComponent;
  }

  getNotebookTags = async () => {
    const path = `/api/notebooks/${this.notebookId}/tags`;
    const response = await this.requestHandler.sendRequestWithToken(path, "GET", null);

    if(!response.ok) {
      this.messageSender.error("Get tags failed");
    }

    return await response.json();
  }

  generateTagComponent = (tag) => {
    const tagComponent = $(`
      <div class="tag js_specific_tag">
        <p class="js_tag_name">${tag.name}</p>
        <div class="deleteTagBtn">
          <p class="js_remove_notebook_tag_btn">Removev</p>
        </div>
      </div>
    `);

    tagComponent.find(".js_remove_notebook_tag_btn").on("click", async (e) => {
      e.stopPropagation();
      const path = `/api/notebooks/${this.notebookId}/tags?tagId=${tag.tagId}`;
      const response = await this.requestHandler.sendRequestWithToken(path, "DELETE", null);
      if(!response.ok) {
        this.messageSender.error("Delete tag failed");
      } else {
        this.messageSender.success("Delete tag success");
        tagComponent.remove();
      }
    })

    return tagComponent;
  }
  resetFilters = () => {
    this.filters = {
      "star": false,
      "tag": "",
      "keyword": "",
      "sortByDesc": false
    }
  }

  generateNoteTagsComponent = (tags, noteTags) => {
    let tagComponents = [];
    tags.forEach((tag) => {
      tagComponents.push(this.generateNoteTagComponent(tag, noteTags));
    })

    return tagComponents;
  }

  generateNoteTagComponent = (tag, noteTags) => {
    const tagComponent = $(`
      <div class="tag js_note_tag data-tagid="${tag.tagId}">
        <img class="checked display-none" src="/icons/check.png" alt="check"/>
        <p>${tag.name}</p>
      </div>
    `);

    // todo 將noteTag從array改為map，提高搜尋效率
    for (const noteTag of noteTags) {
      if(noteTag.tagId === tag.tagId) {
        tagComponent.addClass("js_selected");
        tagComponent.find(".checked").removeClass("display-none");
        break;
      }
    }

    tagComponent.on('click',async (e) => {
      e.stopPropagation();

      if(tagComponent.hasClass("js_selected")) {
        tagComponent.removeClass("js_selected");
        tagComponent.find(".checked").addClass("display-none");
        this.deleteNoteTag(tag.tagId);
      } else {
        tagComponent.addClass("js_selected");
        tagComponent.find(".checked").removeClass("display-none");
        this.addNoteTag(tag.tagId);
      }

    })
    return tagComponent;
  }

  addNoteTag = async (tagId) => {
    const response = await this.requestHandler.sendRequestWithToken(
        `/api/notebooks/${this.notebookId}/notes/${this.noteId}/tags`,
        "POST",
        {
          "tagId": tagId
        }
    );

    if(!response.ok) {
      this.messageSender.error("Add tag failed");
    }
  }

  deleteNoteTag = async (tagId) => {
    const response = await this.requestHandler.sendRequestWithToken(
        `/api/notebooks/${this.notebookId}/notes/${this.noteId}/tags?tagId=${tagId}`,
        "DELETE",
        null
    );
    if(!response.ok) {
      this.messageSender.error("Delete tag failed");
    }
  }
  generateCollaboratorComponent = (collaborator) => {
    return $(`
      <div class="js_collaborator_name_ctn flex">
          <p class="offline js_status_point status_point"></p>
          <p class="user" data-email="${collaborator.userEmail}">${collaborator.name}</p>
      </div> 
    `);
  }
}