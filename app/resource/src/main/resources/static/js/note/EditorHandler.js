const $ = require("jquery");
import Editor from '@toast-ui/editor';
import '@toast-ui/editor/dist/toastui-editor.css';
import '@toast-ui/editor/dist/theme/toastui-editor-dark.css';
import 'prismjs/themes/prism.css';
import '@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight.css';
import codeSyntaxHighlight from '@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight-all';
import {RequestHandler} from "@unityJS/RequestHandler";
export class EditorHandler {
  editor;
  wsConnector;

  operationType = {
    insert: "INSERT",
    delete: "DELETE",
  }
  cursorPosition;
  constructor(noteId, notebookId) {
    this.noteId = noteId;
    this.notebookId = notebookId;
    this.requestHandler = new RequestHandler();
  }

  setWsConnector = (wsConnector) => {
    this.wsConnector = wsConnector;
  }

  generateEditor = async () => {
    const response = await this.requestHandler.sendRequestWithToken(
        `/api/notebooks/${this.notebookId}/notes/${this.noteId}`,
        "GET",
        null
    )
    const data = await response.json();
    const {note} = data;
    $(`.js_note_name`).text(note.name);
    const initialValue = note.content.trim() === "" ? `# Title\n\n## Question\n\n## Keypoint` : note.content;
    this.editor = new Editor({
      el: document.querySelector("#editor"),
      height: "93vh",
      previewStyle: "vertical",
      initialValue: initialValue,
      theme: "dark",
      plugins: [codeSyntaxHighlight],
    });

    this.registerEvents();
  }

  registerEvents = () => {
    this.editor.on('keydown', async (type, event) => {
      console.log("-----以下為keydown事件-----");
      console.log('按下的按鍵代碼:', event.key);

      if (event.key === 'x' && (event.ctrlKey || event.metaKey)) {
        console.log('剪下事件觸發');
        await this.cutEventCallback();
        return;
      }

      if(event.key === "v" && (event.ctrlKey || event.metaKey)) {
        console.log('貼上事件觸發');
        await this.pasteEventCallback();
        return;
      }

      if(event.key === "z" && (event.ctrlKey || event.metaKey)) {
        console.log('撤銷事件觸發');
        await this.withdrawEventCallback();
        return;
        // 在這裡執行你想要的操作
      }

      if(event.key === "Enter") {
        console.log("觸發換行事件");
        this.enterEventCallback();
        return;
      }

      if(event.key === "Tab") {
        this.tabEventCallback();
      }

      if(event.key === "Backspace") {
        console.log("觸發刪除事件");
        this.backspaceEventCallback();
        return;
      }

      if(this.isNotInExcludedKeys(event.key)) {
        console.log("觸發輸入事件");
        this.inputEventCallback(event.key);
      }
    });
  }

  cutEventCallback = async () => {
    const position = this.editor.getSelection();
    console.log(this.editor.getSelectedText(position[0], position[1]));
  }

  pasteEventCallback = async () => {
    const md = this.editor.getMarkdown();

    // console.log(md);
    // const smd = md.split("\n");
    // console.log(smd.length);
    // if(10 > smd.length) {
    //
    // }
  }

  withdrawEventCallback = async () => {

  }

  enterEventCallback = () => {

  }

  tabEventCallback = () => {

  }

  backspaceEventCallback = () => {

  }

  inputEventCallback = (key) => {
    const position = this.editor.getSelection();
    this.wsConnector.sendMessage(key, position, this.operationType.insert);
  }

  appendMessage = (data) => {
    const {content} = data;
    this.editor.insertText(content);
  }

  isNotInExcludedKeys = (key) => {
    const excludedKeys = {
      "Escape": true,
      "CapsLock": true,
      "Shift": true,
      "Alt": true,
      "Control": true,
      "Meta": true,
      "ArrowRight": true,
      "ArrowLeft": true,
      "ArrowUp": true,
      "ArrowDown": true,
      "F1": true,
      "F2": true,
      "F3": true,
      "F4": true,
      "F5": true,
      "F6": true,
      "F7": true,
      "F8": true,
      "F9": true,
      "F10": true,
      "F11": true,
      "F12": true,
    };

    return !(key in excludedKeys);
  }
}