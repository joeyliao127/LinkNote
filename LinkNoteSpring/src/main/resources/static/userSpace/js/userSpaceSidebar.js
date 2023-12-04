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
  genNotebooks(getNotebooksRes.notebooks, myNotebook, false);
  console.log(`產生coNotebooks title`);
  genNotebooks(getcoNotebooksRes.notebooks, coNotebook, true);
}

function genNotebooks(notebookInfos, notebookCtn, isCoNotebook) {
  for (let notebook of notebookInfos) {
    const notebookTitle = document.createElement("div");
    notebookTitle.textContent = notebook.name;
    notebookTitle.dataset.name = notebook.name;
    notebookTitle.dataset.notebookId = notebook.notebookId;
    notebookTitle.dataset.description = notebook.description;

    if (isCoNotebook) {
      notebookTitle.classList.add("coNotebook");
    } else {
      notebookTitle.classList.add("notebook");
    }

    notebookCtn.appendChild(notebookTitle);
  }
  notebooksBtnListener(isCoNotebook);
}

function notebooksBtnListener(isCoNotebook) {
  let notebooks;
  if (isCoNotebook) {
    notebooks = document.querySelectorAll(".coNotebook");
  } else {
    notebooks = document.querySelectorAll(".notebook");
  }

  let selectedId = -1;

  for (let i = 0; i < notebooks.length; i++) {
    notebooks[i].addEventListener("click", async () => {
      if (selectedId == -1) {
        notebooks[i].classList.toggle("selected");
        const newNoteBtn = document.querySelector(".newNoteBtn");
        newNoteBtn.classList.remove("display-none");
      } else {
        notebooks[selectedId].classList.toggle("selected");
        notebooks[i].classList.toggle("selected");
      }
      console.log(`執行genNotes`);
      genNotes(notebooks[i]);
      selectedId = i;
    });
  }
}

let notesDataMap = {};

//傳入notebook物件，取得dataset中的notebookId & name & description
//這個Fn用來產生點選筆記本後，顯示所有筆記card並且設定事件監聽
async function genNotes(notebook) {
  const notebookName = notebook.dataset.name;
  console.log(`執行note maker，notebook name: ${notebookName}`);
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
    console.log(notesDataMap);
  }
  const notebookNameCtn = document.querySelector(".main-group-subjectInfo h2");
  const descriptionCtn = document.querySelector(".main-group-subjectInfo p");
  notebookNameCtn.dataset.notebookId = notesDataMap[notebookName].notebookId;
  notebookNameCtn.textContent = notesDataMap[notebookName].name;
  descriptionCtn.textContent = notesDataMap[notebookName].description;
  const noteCardCtn = document.querySelector(".main-group-notes");

  //切換筆記本，清空note
  noteCardCtn.innerHTML = "";
  console.log(notesDataMap[notebookName].notes);
  notesDataMap[notebookName].notes.forEach((note) => {
    const noteCard = document.createElement("div");
    const star = document.createElement("img");
    const noteName = document.createElement("h3");
    const qeustion = document.createElement("p");
    const createDate = document.createElement("p");
    noteCard.classList.add("main-item-noteCard");
    noteCard.classList.add("flex");
    createDate.classList.add("date");
    if (note.star) {
      star.src = "/static/resource/images/star-full.png";
    } else {
      star.src = "/static/resource/images/star-empty.png";
    }

    star.alt = "star";
    star.classList.add("btn");
    noteName.textContent = note.name;
    noteName.dataset.noteId = note.noteId;
    qeustion.textContent = note.qeustion;
    createDate.textContent = note.createDate.split(" ")[0];
    noteCard.addEventListener("click", () => {
      window.location.href = `/notebooks/${notesDataMap[notebookName].notebookId}/notes/${note.noteId}`;
    });
    noteCard.appendChild(star);
    noteCard.appendChild(noteName);
    noteCard.appendChild(qeustion);
    noteCard.appendChild(createDate);
    noteCardCtn.appendChild(noteCard);
  });
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
  genNotebooks(notebookInfo, myNotebooks, false);
}
//取得notebook -> 觀察notebook -> fetch notebook -> 觀察notebook

sideBarInit();
