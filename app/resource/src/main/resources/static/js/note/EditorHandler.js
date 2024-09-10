const $ = require("jquery");
import Editor from '@toast-ui/editor';
import '@toast-ui/editor/dist/toastui-editor.css';
import '@toast-ui/editor/dist/theme/toastui-editor-dark.css';
import 'prismjs/themes/prism.css';
import '@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight.css';
import codeSyntaxHighlight
  from '@toast-ui/editor-plugin-code-syntax-highlight/dist/toastui-editor-plugin-code-syntax-highlight-all';
import {RequestHandler} from "@unityJS/RequestHandler";
import {MessageSender} from "@unityJS/MessageSender";

export class EditorHandler {
  editor;
  wsConnector;
  latestNoteContent;

  cursorPosition;
  constructor(noteId, notebookId) {
    this.noteId = noteId;
    this.notebookId = notebookId;
    this.requestHandler = new RequestHandler();
    this.messageSender = new MessageSender();
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
    let initialValue = "";
    if (note.content === null || note.content.trim() === "") {
      initialValue = "# Title\n\n## Question\n\n## Keypoint";
    } else {
      initialValue = note.content;
    }
    this.editor = new Editor({
      el: document.querySelector("#editor"),
      height: "93vh",
      previewStyle: "vertical",
      initialValue: initialValue,
      theme: "dark",
      plugins: [codeSyntaxHighlight],
    });

    this.latestNoteContent = initialValue;
    this.editor.setSelection([1,1], [1,1]);
    // this.registerSaveNoteEvent();

    //TODO 實做共編時記得完成
    this.registerEvents();
  }

  registerSaveNoteEvent = () => {
    setInterval(async () => {
      const markdown = this.editor.getMarkdown();
      const path = `/api/notebooks/${this.notebookId}/notes/${this.noteId}`;
      const noteName = $('.js_note_name').text();
      const question = $("h2:contains('Question')").next("p").text();
      const keypoint = $("h2:contains('Keypoint')").next("p").text();
      const response = await this.requestHandler.sendRequestWithToken(path, "PUT",{
        content: markdown,
        question: question,
        keypoint: keypoint,
        name: noteName,
      });

      if (!response.ok) {
        this.messageSender.error("Save note error.");
      }
    }, 3000);
  }

  registerEvents = () => {
    this.editor.on('keyup', async (type, event) => {
      // console.log("-----以下為keyup事件-----");
      // console.log('按下的按鍵代碼:', event.key);
      // console.log(event);

      // if(event.key === "z" && (event.ctrlKey || event.metaKey)) {
      //   console.log('撤銷事件觸發');
      //   await this.withdrawEventCallback();
      //   return;
      // }
      //
      // // 如果沒有監聽，會傳送c文字
      // if(event.key === "c" && (event.ctrlKey || event.metaKey)) {
      //   return;
      // }
      //
      // // 如果沒有監聽，會傳送a文字
      // if(event.key === "a" && (event.ctrlKey || event.metaKey)) {
      //   return;
      // }
      //
      // if(event.key === "Tab") {
      //   this.tabEventCallback();
      //   return;
      // }
      //
      // // 如果有按下其中一個key，代表再執行快捷鍵操作，無需進行文本比較
      // if(event.altKey || event.metaKey || event.ctrlKey || event.shiftKey) {
      //   return;
      // }
      //
      // // 上面的 or 判斷和 isNotInExcludeKeys 判斷不一樣，一個是比較 event.key，另一個是比較event.xxx
      // // 除非單純輸入文字，且為操作key以外
      // if(this.isNotInExcludedKeys(event.key)) {
      //   console.log("觸發輸入事件");
      //   this.inputEventCallback(event.key);
      // }
    });

    this.editor.on("keydown", async (type, event) => {
      console.log("-----以下為keydown事件-----");
      console.log('按下的按鍵代碼:', event.key);
      console.log(event);

      if(event.key === "z" && (event.ctrlKey || event.metaKey)) {
        console.log('撤銷事件觸發');
        await this.withdrawEventCallback();
        return;
      }

      // 如果沒有監聽，會傳送c文字
      if(event.key === "c" && (event.ctrlKey || event.metaKey)) {
        return;
      }

      // 如果沒有監聽，會傳送a文字
      if(event.key === "a" && (event.ctrlKey || event.metaKey)) {
        return;
      }

      if(event.key === "Tab") {
        this.tabEventCallback();
        return;
      }

      if(event.key === "Backspace") {
        console.log("觸發刪除事件");
        this.backspaceEventCallback();
        return;
      }

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

      if(event.key === "Enter") {
        console.log("觸發換行事件");
        this.enterEventCallback();
        return;
        // this.inputEventCallback(event.key);
      }

      // 如果有按下其中一個key，代表再執行快捷鍵操作，無需進行文本比較
      if(event.altKey || event.metaKey || event.ctrlKey || event.shiftKey) {
        return;
      }


      // 上面的 or 判斷和 isNotInExcludeKeys 判斷不一樣，一個是比較 event.key，另一個是比較event.xxx
      // 除非單純輸入文字，且為操作key以外
      if(this.isNotInExcludedKeys(event.key)) {
        console.log("觸發輸入事件");
        this.inputEventCallback(event.key);
      }

    })
  }

  //剪下事件
  cutEventCallback = async () => {
    const position = this.editor.getSelection();
    this.wsConnector.sendDeleteMessage(position);
  }

  //貼上事件
  pasteEventCallback = async () => {
    const message = await navigator.clipboard.readText();
    const position = this.editor.getSelection();
    console.log(message);
    this.wsConnector.sendInsertMessage(message, position);
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
    const latestText = this.latestNoteContent;
    const updatedText = this.editor.getMarkdown();
    console.log("----------------分隔線----------------");
    const textAndPosition = this.compareTextDifference(latestText, updatedText);
    this.wsConnector.sendInsertMessage(textAndPosition.text, [textAndPosition.position, textAndPosition.position]);
  }

  compareTextDifference = (latestText, updatedText) => {
    const cursorPosition = this.editor.getSelection()[0];
    const cursorRowPosition = cursorPosition[0];
    const cursorColumnPosition = cursorPosition[1];
    console.log(`當前cursor row 座標行數：${cursorRowPosition}`);
    console.log(`當前cursor column 座標行數：${cursorColumnPosition}`);
    const latestList = latestText.split("\n");
    const updatedList = updatedText.split("\n");
    console.log("last note");
    console.log(latestList);
    console.log("updatedList note");
    console.log(updatedList);
    const lineGapDifferent = updatedList.length - latestList.length;
    console.log("差距行數：" + lineGapDifferent);

    // 如果沒有差別，代表變更內文在同一行，比較同一行即可
    if(lineGapDifferent === 0) {
      const differentRowPosition = cursorPosition[0] - 1; // array從0開始，但getSelection是從1開始
      const textAndColumnPosition = this.getDifferentTextAndPosition(latestList[differentRowPosition], updatedList[differentRowPosition]);
      console.log("同一行不同的文字:" + textAndColumnPosition["text"]);
      console.log("position:" + textAndColumnPosition["position"]);
      return {
        position: [cursorPosition[0], textAndColumnPosition.position],
        text: textAndColumnPosition.text,
      }
    }

    let differentText = "";
    //起始變更行數：cursor當前位址 - 新增行數 - 1，比如第六行，新增兩行，
    /*
    ---4 old 這是arr[3]
    ---5 新增 這是arr[4]
    ---6 新增 這是arr[5] cursor在第六行 = arr[5]，新增的一行 arr[5 - 1]
     */
    const startChangeRowPosition = cursorPosition[0] - lineGapDifferent - 1;
    const textAndColumnPosition = this.getDifferentTextAndPosition(latestList[startChangeRowPosition], updatedList[startChangeRowPosition])
    differentText = textAndColumnPosition.text;

    for(let i = lineGapDifferent; i > 0; i--) {
      const rowText = updatedList[updatedList.length - i - 1] + "\n";
      differentText += rowText;
    }
    // 從當前cursor行數的字串，累加到改變行數的下一行
    // for (let i=firstDifferentRowPosition; i<= cursorPosition[0], i++;) {
    //   differentText = updatedList[i] + "\n";
    // }
    console.log("累加後的text: " + differentText);

    return {
      position: [cursorPosition[0], textAndColumnPosition.position],
      text: differentText
    }
  }

  // 取得同一行的差異文字與起始差異位址
  getDifferentTextAndPosition = (latestRowText, updatedRowText) => {
    const latestRowTextLength = latestRowText.length;
    const updatedRowTextLength = updatedRowText.length;
    const textChangeAmount = updatedRowTextLength - latestRowTextLength;
    const startIndex = updatedRowTextLength - textChangeAmount;
    return {
      position: startIndex + 1,
      text: textChangeAmount === 0 ? "" : updatedRowText.substring(startIndex, updatedRowTextLength),
    }
  }

  // 插入接收到的訊息
  appendMessage = (data) => {
    console.log("收到來自後端的SEND訊息");
    console.log(data);
    const {content} = data;
    const {position} = data;
    const currentPosition = this.editor.getSelection();
    this.editor.setSelection(position[0], position[1]);
    this.editor.insertText(content);
    this.latestNoteContent = this.editor.getMarkdown(); //接收訊息並insert之後，要更新last版本的text
    this.editor.setSelection(currentPosition[0], currentPosition[1]);
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
      "Backspace": true,
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