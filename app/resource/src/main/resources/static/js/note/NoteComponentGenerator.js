import Editor from 'tui-editor';
import 'tui-editor/dist/tui-editor.css'; // editor's ui
import 'tui-editor/dist/tui-editor-contents.css'; // editor's content
import 'codemirror/lib/codemirror.css'; // codemirror
import 'highlight.js/styles/github.css'; // code block highlight

export class NoteComponentGenerator {
  constructor(notebookId) {
    this.filters = {
      "allNotes": true,
      "starred": false,
      "tag": "",
      "keyword": "",
      "sortByDesc": false
    }
  }

}