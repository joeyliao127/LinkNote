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
  genNotebooksBtn(getNotebooksRes.notebooks, myNotebook, false);
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
    const lastReadNotebookId = localStorage.getItem("notebookId");

    if (!lastReadNotebookId) {
      localStorage.setItem("notebookId", notebookBtn.dataset.notebookId);
      genNotesCardCtn(
        notebookBtn.dataset.name,
        notebookBtn.dataset.notebookId,
        notebookBtn.dataset.description
      );
      return;
    }
    const lastSelectedNotebookBtn = document.querySelector(
      `.myNotebooks .notebook[data-notebook-id = '${lastReadNotebookId}'`
    );
    const currentNotebookId = notebookBtn.dataset.notebookId;
    lastSelectedNotebookBtn.classList.remove("selected");
    notebookBtn.classList.toggle("selected");
    localStorage.setItem("notebookId", currentNotebookId);

    const path = `/api/notebooks/${currentNotebookId}/notes?offset=0&limit=20`;
    genNotesCardCtn(
      notebookBtn.dataset.name,
      notebookBtn.dataset.notebookId,
      notebookBtn.dataset.description,
      path
    );

    genNotebookTagItems(notebookBtn.dataset.notebookId);
  });
}

async function genNotebookTagItems(notebookId) {
  const path = `/api/notebooks/${notebookId}/tags`;
  console.log(path);
  const tagDatas = await fetchData(path, `GET`);
  const tagCtn = document.querySelector(".tagList");
  for (let tagData of tagDatas.tag) {
    const tagBtn = genTagItemBtn(tagData);

    tagCtn.appendChild(tagBtn);
  }
}

function genTagItemBtn(tagData) {
  const tagItem = document.createElement("div");
  const tagBtn = document.createElement("p");
  const trashBtn = document.createElement("img");
  tagItem.classList.add("tagItem");
  tagItem.classList.add("flex");
  tagBtn.textContent = tagData.name;
  trashBtn.src = "/static/resource/images/trash-white.png";
  trashBtn.dataset.tagName = tagData.name;
  trashBtn.dataset.tagid = tagData.tagId;

  tagBtn.addEventListener("click", () => {
    //Fn程式碼在userSpaceNoteConsole.js，因為屬於filter操作
  });

  trashBtn.addEventListener("click", async () => {
    const notebookId = localStorage.getItem("notebookId");
    const path = `/api/notebooks/${notebookId}/tags?tag=${tagData.name}`;
    const result = fetchData(path, "DELETE");
    if (result.result) {
      tagItem.remove();
      MsgMaker.success("removed!");
    } else {
      MsgMaker.error("removed failed.");
    }
  });
  tagItem.appendChild(tagBtn);
  tagItem.appendChild(trashBtn);
  return tagItem;
}

async function genNotesCardCtn(notebookName, notebookId, description, path) {
  const result = await fetchData(path, "GET");
  const notebookNameCtn = document.querySelector(".main-group-subjectInfo h2");
  const descriptionCtn = document.querySelector(".main-group-subjectInfo p");
  notebookNameCtn.dataset.notebookId = notebookId;
  notebookNameCtn.textContent = notebookName;
  descriptionCtn.textContent = description;
  const noteCardCtn = document.querySelector(".main-group-notes");

  //切換筆記本，清空note
  noteCardCtn.innerHTML = "";
  result.notes.notes.forEach((note) => {
    const noteCard = genNoteCard(note, notebookId);
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

    const starStatus = {};
    if (star.dataset.star === "true") {
      starStatus.star = false;
      star.dataset.star = false;
    } else {
      starStatus.star = true;
      star.dataset.star = true;
    }
    const result = await fetchData(path, "PUT", starStatus);

    if (result.result) {
      MsgMaker.success("updated!");
    } else {
      MsgMaker.error("failed");
    }
    if (star.dataset.star === "true") {
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
  genNotebooksBtn(notebookInfo, myNotebooks, false);
}
//取得notebook -> 觀察notebook -> fetch notebook -> 觀察notebook

sideBarInit();
