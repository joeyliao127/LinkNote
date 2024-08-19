const $ = require("jquery");
import {RequestHandler} from "@unityJS/RequestHandler";
import {NoteCardComponentFactory} from "@notebookJS/componentFactory/NoteCardComponentFactory";
import {NoteFilterComponentFactory} from "@notebookJS/componentFactory/NoteFilterComponentFactory";

export class OwnerNotebookFactory {
  noteCardComponentFactory = new NoteCardComponentFactory();
  noteFilterComponentFactory = new NoteFilterComponentFactory();

  renderOwnerNotebooks = () => {

  }

  renderSpecificOwnerNotebook = () => {

  }

  displayCreateNotebookElement() {
    $('.js_init_create_notebook_wrapper').show();
  }
}

export class CollaborativeNotebookFactory {
  noteCardComponentFactory;
  noteFilterComponentFactory;

  constructor() {
    this.noteCardComponentFactory = new NoteCardComponentFactory();
    this.noteFilterComponentFactory =  new NoteFilterComponentFactory();
  }
  renderCollaborativeNotebooks = () => {

  }

  renderSpecificCollaborativeNotebook = () => {

  }
}






