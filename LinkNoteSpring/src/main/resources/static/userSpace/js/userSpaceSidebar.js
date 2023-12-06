function sideBarInit() {
  switchNotebooksTpyeBtn();
  newNotbookBtnListener();
  signOutBtn();
  setUserInfo();
  setNotebooks();
}

function switchNotebooksTpyeBtn() {
  const switchNotebookBtn = document.querySelector(
    ".sideBar-group-notebooksType img"
  );

  const typeName = document.querySelector(".sideBar-group-notebooksType p");
  const myNotebooksCtn = document.querySelector(".myNotebooks");
  const coNotebookCtn = document.querySelector(".coNotebooks");
  let flag = true;
  switchNotebookBtn.addEventListener("click", () => {
    if (flag) {
      typeName.textContent = "CoNotebooks";
      flag = false;
    } else {
      typeName.textContent = "MyNoteBooks";
      flag = true;
    }
    myNotebooksCtn.classList.toggle("toCoNotebooks");
    coNotebookCtn.classList.toggle("toCoNotebooks");
  });
}

function newNotbookBtnListener() {
  const newNoteBookBtn = document.querySelector(".sideBar-newNotebookBtn");
  newNoteBookBtn.addEventListener("click", switchUserSpacePage);
}

function switchUserSpacePage() {
  const noteConsoleArea = document.querySelector(".main-area-noteConsole");
  const createNotebookArea = document.querySelector(
    ".main-area-createNotebook"
  );
  noteConsoleArea.classList.toggle("toForm");
  createNotebookArea.classList.toggle("toForm");
  noteConsoleArea.classList.toggle("toNote");
  createNotebookArea.classList.toggle("toNote");
}
function signOutBtn() {
  const btn = document.querySelector(".sideBar-item-signoutBtn");
  btn.addEventListener("click", () => {
    localStorage.removeItem("token");
    window.location.href = "/";
  });
}

async function setUserInfo() {
  const data = await fetchData("/api/user", "GET");
  const username = document.querySelector(".sideBar-ctn-welcome p");
  username.textContent = data.username;
  const email = document.querySelector(".sideBar-item-userEmail p");
  email.textContent = data.email;
}

async function setNotebooks() {
  const myNotebook = document.querySelector(".myNotebooks");
  const coNotebook = document.querySelector(".coNotebooks");
  const notebookPath = `/api/notebooks?offset=0&limit=20&coNotebook=false`;
  const coNotebookPath = `/api/notebooks?offset=0&limit=20&coNotebook=true`;
  const getNotebooksRes = await fetchData(notebookPath, "GET");
  const getcoNotebooksRes = await fetchData(coNotebookPath, "GET");
  console.log(`產生notebooks title`);
  genNotebooksBtn(getNotebooksRes.notebooks, myNotebook, false);
  console.log(`產生coNotebooks title`);
  genNotebooksBtn(getcoNotebooksRes.notebooks, coNotebook, true);
}

async function genNotebooksBtn(notebookInfos, notebookCtn, isCoNotebook) {
  for (let notebook of notebookInfos) {
    const notebookBtn = document.createElement("div");
    const notebookName = notebook.name;
    notebookBtn.textContent = notebookName;
    notebookBtn.dataset.name = notebookName;
    notebookBtn.dataset.notebookId = notebook.notebookId;
    notebookBtn.dataset.description = notebook.description;
    if (isCoNotebook) {
      notebookBtn.classList.add("coNotebook");
    } else {
      notebookBtn.classList.add("notebook");
    }
    genNotebooksBtnListener(notebookBtn);
    notebookCtn.appendChild(notebookBtn);
  }
}

function genNotebooksBtnListener(notebookBtn) {
  notebookBtn.addEventListener("click", () => {
    const lastReadNotebookId = localStorage.getItem("noteookId");

    if (!lastReadNotebookId) {
      localStorage.setItem("notebookId", notebookBtn.dataset.notebookId);
      genNotes(notebookBtn);
      genNotebookTagItems(notebookBtn.dataset.notebookId);
      return;
    }
    const lastSelectedNotebookBtn = document.querySelector(
      `.myNotebooks .notebook[data-notebook-id = '${lastReadNotebookId}'`
    );
    console.log(lastSelectedNotebookBtn);
    lastSelectedNotebookBtn.classList.remove("selected");
    notebookBtn.classList.toggle("selected");
    genNotes(notebookBtn);
    genNotebookTagItems(
      notebookBtn.dataset.notebookName,
      notebookBtn.dataset.notebookId
    );
  });
}
let notebookTagMap = {};
async function genNotebookTagItems(notebookName, notebookId) {
  if (!notebookTagMap.hasOwnProperty(notebookName)) {
    const path = `/api/notebooks/${notebookId}/tags`;
    const result = await fetchData(path, "GET");
    console.log(result);
    notebookTagMap[notebookName] = result;
    console.log(notebookTagMap.notebookName);
  }
}

let notesDataMap = {};

//傳入notebook物件，取得dataset中的notebookId & name & description
//這個Fn用來產生點選筆記本後，顯示所有筆記card並且設定事件監聽
async function genNotes(notebook) {
  console.log(notesDataMap);
  const notebookName = notebook.dataset.name;
  //如果在notesDataMap(暫存器)中找到key，新增key
  if (!notesDataMap.hasOwnProperty(notebookName)) {
    notesDataMap[notebookName] = {};
    notesDataMap[`${notebookName}`]["name"] = notebookName;
    notesDataMap[`${notebookName}`]["description"] =
      notebook.dataset.description;
    notesDataMap[`${notebookName}`]["notebookId"] = notebook.dataset.notebookId;
    const path = `/api/notebooks/${notebook.dataset.notebookId}/notes?offset=0&limit=20`;
    const result = await fetchData(path, "GET");
    notesDataMap[notebookName]["notes"] = result.notes;
  }
  const notebookNameCtn = document.querySelector(".main-group-subjectInfo h2");
  const descriptionCtn = document.querySelector(".main-group-subjectInfo p");
  notebookNameCtn.dataset.notebookId = notesDataMap[notebookName].notebookId;
  notebookNameCtn.textContent = notesDataMap[notebookName].name;
  descriptionCtn.textContent = notesDataMap[notebookName].description;
  const noteCardCtn = document.querySelector(".main-group-notes");

  //切換筆記本，清空note
  noteCardCtn.innerHTML = "";
  console.log(notesDataMap);
  notesDataMap[notebookName].notes.notes.forEach((note) => {
    const noteCard = genNoteCard(note, notesDataMap[notebookName].notebookId);
    noteCardCtn.appendChild(noteCard);
  });
}

function genNoteCard(note, notebook) {
  const noteCard = document.createElement("div");
  let star = document.createElement("img");
  const noteName = document.createElement("h3");
  const question = document.createElement("p");
  const createDate = document.createElement("p");
  noteCard.classList.add("main-item-noteCard");
  noteCard.classList.add("flex");
  createDate.classList.add("date");
  if (note.star) {
    star.src = "/static/resource/images/star-full.png";
    star.dataset.star = true;
  } else {
    star.src = "/static/resource/images/star-empty.png";
    star.dataset.star = false;
  }

  star.alt = "star";
  star.classList.add("btn");
  noteName.textContent = note.name;
  noteName.dataset.noteId = note.noteId;
  question.textContent = note.question;
  createDate.textContent = note.createDate.split(" ")[0];
  star = genNoteStarListener(star, notebook, note.noteId);
  //建立noteCard監聽事件
  noteCard.addEventListener("click", () => {
    localStorage.setItem(
      "notebookName",
      document.querySelector(".main-group-subjectInfo h2").textContent
    );
    localStorage.setItem("noteId", note.noteId);
    window.location.href = `/notebooks/${notebook}/notes/${note.noteId}`;
  });

  noteCard.appendChild(star);
  noteCard.appendChild(noteName);
  noteCard.appendChild(question);
  noteCard.appendChild(createDate);
  return noteCard;
}

function genNoteStarListener(star, notebookId, noteId) {
  star.addEventListener("click", async (e) => {
    e.stopPropagation();
    const path = `/api/notebooks/${notebookId}/notes/${noteId}/star`;
    console.log(`path = ${path}`);
    const starStatus = {};
    if (star.dataset.star === "true") {
      starStatus.star = false;
      star.dataset.star = false;
    } else {
      starStatus.star = true;
      star.dataset.star = true;
    }
    const result = await fetchData(path, "PUT", starStatus);
    console.log(result);
    if (result.result) {
      MsgMaker.success("updated!");
    } else {
      MsgMaker.error("failed");
    }
    if (star.dataset.star === "true") {
      console.log(star.dataset.star);
      console.log(star.dataset.star === "true");
      star.src = "/static/resource/images/star-full.png";
    } else {
      star.src = "/static/resource/images/star-empty.png";
    }
  });
  return star;
}

function removeNotebookBtn(notebookId) {
  const notebookList = document.querySelectorAll(".notebook");
  notebookList.forEach((notebook) => {
    if (notebook.dataset.notebookId === notebookId) {
      notebook.remove();
    }
  });
}

function createOneNotebookBtn(name, description, notebookId) {
  const myNotebooks = document.querySelector(".myNotebooks");
  let notebookInfo = [
    {
      notebookId,
      name,
      description,
    },
  ];
  console.log(notebookInfo);
  genNotebooksBtn(notebookInfo, myNotebooks, false);
}
//取得notebook -> 觀察notebook -> fetch notebook -> 觀察notebook

sideBarInit();
