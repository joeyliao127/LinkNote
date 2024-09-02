const $ = require("jquery");
import {
  NoteComponentFactory
} from "@notebookJS/componentFactory/NoteComponentFactory";

export class OwnerNotebookComponentFactory {
  constructor() {
    this.noteCardComponentFactory = new NoteComponentFactory({
          createNoteBtn: true,
          invitationBtn: false,
          allNoteBtn: false,
          tagBtn: false,
          starBtn: false,
          sortBtn: false,
          keyword: false,
          deleteNoteBtn: true
        });
  }

  generateOwnerNotebooks = (notebooks) => {
    const notebookCtn = $(`.js_owner_notebook_ctn`);
    notebooks.forEach(async (notebook) => {
      const noteCtn = await this.noteCardComponentFactory.genNoteCtn(notebook, "owner");
      notebookCtn.append(noteCtn);
      notebookCtn.append($(`<div class="divide"></div>`));
    })

    return notebookCtn;
  }
}

export class CollaborativeNotebookComponentFactory {

  constructor() {
    this.noteCardComponentFactory = new NoteComponentFactory({
      createNoteBtn: true,
      invitationBtn: false,
      allNoteBtn: false,
      tagBtn: false,
      starBtn: false,
      sortBtn: false,
      keyword: false,
      deleteNoteBtn: false
    });
  }
  generateCollaborativeNotebooks = (notebooks) => {
    const notebookCtn = $(`.js_collaborative_notebook_ctn`);
    notebooks.forEach(async (notebook) => {
      const noteCtn = await this.noteCardComponentFactory.genNoteCtn(notebook, "collaborate");
      notebookCtn.append(noteCtn);
      notebookCtn.append($(`<div class="divide"></div>`));
    })

    return notebookCtn;
  }
}

export class SpecificOwnerNotebookComponentFactory {

  constructor() {
    this.noteCardComponentFactory = new NoteComponentFactory({
      createNoteBtn: true,
      invitationBtn: true,
      allNoteBtn: true,
      tagBtn: true,
      starBtn: true,
      sortBtn: true,
      keyword: true,
      deleteNoteBtn: true
    });
  }
  generateSpecificOwnerNotebook = async (notebook) => {
    return await this.noteCardComponentFactory.genNoteCtn(notebook, "specificOwner");
  }
}

export class SpecificCollaborativeNotebookComponentFactory {

  constructor() {
    this.noteCardComponentFactory = new NoteComponentFactory({
          createNoteBtn: true,
          invitationBtn: false,
          allNoteBtn: true,
          tagBtn: true,
          starBtn: true,
          sortBtn: true,
          keyword: true,
          deleteNoteBtn: false
    });
  }
  generateSpecificCollaborativeNotebook = async (notebook) => {
     return await this.noteCardComponentFactory.genNoteCtn(notebook, "specificCollaborative")
  }
}






