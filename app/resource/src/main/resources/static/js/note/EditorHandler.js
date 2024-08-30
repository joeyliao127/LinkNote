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

  //剪下事件
  cutEventCallback = async () => {
    const position = this.editor.getSelection();
    this.wsConnector.sendDeleteMessage(position);
  }

  //貼上事件
  pasteEventCallback = async () => {
    const md = this.editor.getMarkdown();
    const message = await navigator.clipboard.readText();
    const position = this.editor.getSelection();
    console.log(message);
    this.wsConnector.sendInsertMessage(message, position)

    // console.log(md);
    // const smd = md.split("\n");
    // console.log(smd.length);
    // if(10 > smd.length) {
    //
    // }
  }

  withdrawEventCallback = async () => {
    //TODO 想想要怎麼實踐Undo
  }

  enterEventCallback = () => {
    const message = "\n";
    const position = this.editor.getSelection();
    this.wsConnector.sendInsertMessage(message, position);
  }

  tabEventCallback = () => {
    const message = "    ";
    const position = this.editor.getSelection();
    this.wsConnector.sendInsertMessage(message, position);
  }

  backspaceEventCallback = () => {
    const position = this.editor.getSelection();
    //如果游標沒有選到任何字
    if(
        position[0][0] === position[1][0] &&
        position[0][1] === position[1][1]
    ) {
      position[0][1] -= 1;
    }

    this.wsConnector.sendDeleteMessage(position);
  }

  inputEventCallback = (message) => {
    const position = this.editor.getSelection();
    this.wsConnector.sendInsertMessage(message, position);
  }

  // 插入接收到的訊息
  appendMessage = (data) => {
    console.log("收到來自後端的SEND訊息");
    console.log(data);
    const {content} = data;
    this.editor.insertText(content);
  }

  // 刪除接收到的訊息
  deleteMessage = (data) => {
    const {position} = data;
    const start = position[0];
    const end = position[1];
    this.editor.deleteSelection(start, end);
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