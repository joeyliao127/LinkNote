const $ = require("jquery");
import {RequestHandler} from "@unityJS/RequestHandler";
import Swal from "sweetalert2";
import {MessageSender} from "@unityJS/MessageSender";
import {DeleteAlert} from "@unityJS/DeleteAlert";
import {NoteToolBarComponentFactory} from "@notebookJS/componentFactory/NoteToolBarComponentFactory";

export class NoteComponentFactory {
  requestHandler = new RequestHandler();
  messageSender = new MessageSender();
  deleteAlert = new DeleteAlert();
  constructor(filterType) {
    this.filterType = filterType;
  }
  genNoteCtn = async (notebook, type) => {
    const noteCtn = $(`
      <section class="noteCtn">
        <div class="toolBar">
        </div>
        <h4 class="js_notebook_name">${notebook.name}</h4>
        <p class="description js_edit_description" contenteditable="true">${notebook.description}</p>
        <div class="updateBtnCtn">
          <div class="updateBtn display-none">Save</div>
          <div class="cancelUpdateBtn display-none">Cancel</div>
        </div>
        <h5>Notes</h5>
        <div class="noteCardCtn">
         
        </div>
        <div class="none no_notes display-none">There is no notes.</div>
      </section>
    `);
    this.appendNotebookToolBar(noteCtn, notebook);

    const notesData = await this.getNotes(notebook.id);

    if(notesData.length === 0){
      noteCtn.find(".none").removeClass("display-none");
    }
    notesData.forEach((note) => {
      noteCtn.find(".noteCardCtn").append(this.genNoteCard(note, notebook.id));
    })
    this.registerNoteCtnElementEvent(noteCtn, notebook, type);
    console.log(noteCtn);
    return noteCtn;
  }
  appendNotebookToolBar = (noteCtn, notebook) => {
    const noteFilterComponentFactory = new NoteToolBarComponentFactory(notebook);
    Object.entries(this.filterType).forEach(async ([key, value]) => {
      if(!value) {
        return;
      }
      switch (key) {
        case "createNoteBtn":
            noteCtn.find(".toolBar").append(noteFilterComponentFactory.genCreateNoteComponent());
          break;
        case "invitationBtn":
            noteCtn.find(".toolBar").append(noteFilterComponentFactory.genInvitationBtnComponent());
            noteCtn.find(".toolBar").append(await noteFilterComponentFactory.genInvitationForm());
          break;
        case "allNoteBtn":
            noteCtn.find(".toolBar").append(noteFilterComponentFactory.genAllNoteComponent());
          break;
        case "tagBtn":
          noteCtn.find(".toolBar").append(noteFilterComponentFactory.genTagComponent());
          noteCtn.find(".toolBar").append(await noteFilterComponentFactory.genTagFilterForm());
          break;
        case "sortBtn":
            noteCtn.find(".toolBar").append(noteFilterComponentFactory.genSortComponent());
          break;
        case "starBtn":
          noteCtn.find(".toolBar").append(noteFilterComponentFactory.genStarComponent());
          break;
        case "keyword":
            noteCtn.find(".toolBar").append(noteFilterComponentFactory.genSearchComponent());
          break;
        case "deleteNoteBtn":
            noteCtn.find(".toolBar").append(noteFilterComponentFactory.genDeleteComponent());
          break;
    }});
  }

  appendSpecificCollaborativeNotebookToolBar = (noteCtn, notebook) => {

  }

  //註冊noteCtn底下的html元素 event
  registerNoteCtnElementEvent = (noteCtn, notebook, type) => {

    //刪除notebook事件
    // noteCtn.find(".toolBtn").on("click", () => {
    //   this.deleteNotebook(notebook.id);
    // });

    // 更新description事件
    const descriptionElement = noteCtn.find('.js_edit_description');

    if(type === "owner") {
      descriptionElement.on('click', () => {
        this.editMode(noteCtn);
      });
    }

    const updateBtn = noteCtn.find('.updateBtn');
    updateBtn.on('click', async () => {
      const notebookName = noteCtn.find('.js_notebook_name').text();
      const description = noteCtn.find('.js_edit_description').text();
      await this.updateNotebook(notebook.id, notebookName, description);
      this.cancelEditMode(noteCtn);
    });

    const cancelUpdateBtn = noteCtn.find('.cancelUpdateBtn');
    cancelUpdateBtn.on('click', () => {
      this.cancelEditMode(noteCtn);
    });
  }

  editMode = (noteCtn) => {
    noteCtn.find('.js_edit_description').addClass('editMode');
    noteCtn.find('.updateBtn').removeClass("display-none");
    noteCtn.find('.cancelUpdateBtn').removeClass("display-none");
  }

  cancelEditMode = (noteCtn) => {
    noteCtn.find('.js_edit_description').removeClass('editMode');
    noteCtn.find('.updateBtn').addClass("display-none");
    noteCtn.find('.cancelUpdateBtn').addClass("display-none");
  }

  updateNotebook = async (notebookId, name, description) => {
    const path = `/api/notebooks/${notebookId}`;
    const requestBody = {
      name: name,
      description: description
    }
    const response = await this.requestHandler.sendRequestWithToken(path, "PUT", requestBody);

    if (response.ok) {
      this.messageSender.success("update success!");
    } else {
      this.messageSender.error("update failed.");
    }
  }

  deleteNotebook = (notebookId) => {
    Swal.fire({
      title: 'Warning!',
      text: 'Are you sure you want to delete this notebook?',
      icon: 'warning',
      confirmButtonText: 'yes',
      showCancelButton: true,
      cancelButtonText: 'no',
    }).then(async (result) => {
      if (result.isConfirmed) {
        const path = `/api/notebooks/${notebookId}`;
        const response = await this.requestHandler.sendRequestWithToken(path, "DELETE", null);
        if (response.ok) {
          Swal.fire({
            title: 'Success!',
            text: 'Delete notebook success!',
            icon: 'success',
            confirmButtonText: 'OK',
          }).then((res)=>{
            if (res.isConfirmed) {
              window.location.reload();
            }
          })
        } else {
          this.messageSender.error("Delete notebook failed");
        }
      }
    })
  }

  genNoteCard = (note, notebookId) => {
    let card = $(`
       <a class="noteCard" href="/notebooks/${notebookId}/notes/${note.noteId}">
        <div class="star ${note.star ? "star-full" : "star-empty"}"></div>
        <h5>${note.name}</h5>
        <p class="question">${note.question}</p>
        <div class="deleteNoteBtn"></div>
        <p class="createTime">${note.createDate}</p>
      </a>
    `);

    const starBtn = card.find('.star');
    starBtn.on('click', (e) => {
      e.preventDefault();
      e.stopPropagation();
      if(starBtn.hasClass("star-full")) {
        note.star = false;
        starBtn.addClass('star-empty');
        starBtn.removeClass('star-full');
      } else {
        note.star = true;
        starBtn.removeClass("star-empty");
        starBtn.addClass("star-full");
      }
      this.updateNoteStar(note.name, notebookId, note.noteId, note.star);
    })

    const deleteNoteBtn = card.find('.deleteNoteBtn');
    deleteNoteBtn.on('click', (e) => {
      e.preventDefault();
      e.stopPropagation();
      this.deleteAlert.renderDeleteAlertBox(
          "Note",
          note.name,
          () => {
            this.deleteNote(notebookId, note.noteId, note.name);
            card.remove();
          }
      );
    })
    return card;
  }

  getNotes = async (
      notebookId,
      offset=0,
      limit=20,
      star=false,
      tag=null,
      sortDesc=false,
      keyword=null
  ) => {
    let path = `/api/notebooks/${notebookId}/notes?limit=${limit}`;

    if(star) {
      path += `&star=${star}`;
    }
    if(tag) {
      path += `&tag=${tag}`;
    }
    if(sortDesc) {
      path += `&sortDesc=${sortDesc}`;
    }
    if(keyword) {
      path += `&keyword=${keyword}`;
    }

    let notes = [];
    while (true) {
      path += `&offset=${offset}`;
      const response = await this.requestHandler.sendRequestWithToken(path, "GET");
      const data = await response.json();
      notes.push(...data.notes);
      if (!data.nextPage) {
        break;
      }
    }
    return notes;
  }

  updateNoteStar = async (noteName, notebookId, noteId, star) => {
    const path = `/api/notebooks/${notebookId}/notes/${noteId}`;
    const requestBody = { noteName, star };
    const response = await this.requestHandler.sendRequestWithToken(path, "PUT", requestBody);
    if (response.ok) {
      this.messageSender.success("update success!");
    } else {
      this.messageSender.error("update failed.");
    }
  }

  deleteNote = async (notebookId, noteId) => {
    const path = `/api/notebooks/${notebookId}/notes/${noteId}`;
    const response = await this.requestHandler.sendRequestWithToken(path, "DELETE");
    if (response.ok) {
      this.messageSender.success("delete success!");
    } else {
      this.messageSender.error("delete failed.");
    }
  }
}