const $ = require("jquery");
import {NoteCardComponentFactory} from "@notebookJS/componentFactory/NoteCardComponentFactory";
import {NoteFilterComponentFactory} from "@notebookJS/componentFactory/NoteFilterComponentFactory";

export class OwnerNotebookComponentFactory {
  noteCardComponentFactory = new NoteCardComponentFactory();
  noteFilterComponentFactory = new NoteFilterComponentFactory();


  generateOwnerNotebooks = (notebooks) => {
    const notebookCtn = $(`.js_owner_notebook_ctn`);
    notebooks.forEach(async (notebook) => {
      const noteCardCtn = await this.noteCardComponentFactory.genNoteCtn(notebook, "owner");
      notebookCtn.append(noteCardCtn);
      notebookCtn.append($(`<div class="divide"></div>`));
    })

    return notebookCtn;
  }

  renderSpecificOwnerNotebook = () => {

  }
}

export class CollaborativeNotebookComponentFactory {
  noteCardComponentFactory;
  noteFilterComponentFactory;

  constructor() {
    this.noteCardComponentFactory = new NoteCardComponentFactory();
    this.noteFilterComponentFactory =  new NoteFilterComponentFactory();
  }
  generateCollaborativeNotebooks = (notebooks) => {
    const notebookCtn = $(`.js_collaborative_notebook_ctn`);
    notebooks.forEach(async (notebook) => {
      const noteCardCtn = await this.noteCardComponentFactory.genNoteCtn(notebook, "collaborate");
      notebookCtn.append(noteCardCtn);
      notebookCtn.append($(`<div class="divide"></div>`));
    })

    return notebookCtn;
  }

  renderSpecificCollaborativeNotebook = () => {

  }
}






