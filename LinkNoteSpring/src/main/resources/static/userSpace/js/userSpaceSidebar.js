function sideBarInit() {
  initLocalStorageValue();
  switchNotebooksTpyeBtn();
  newNotbookBtnListener();
  signOutBtn();
  setUserInfo();
  setNotebooks();
}

function initLocalStorageValue() {
  localStorage.setItem("notebookId", null);
  localStorage.setItem("notebookName", null);
  localStorage.setItem("noteId", null);
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
    notebookBtn.classList.add("notebook");
    genNotebooksBtnListener(notebookBtn, isCoNotebook);
    notebookCtn.appendChild(notebookBtn);
  }
}
function filterInit() {
  filter.noteBox = true;
  filter.keyword = null;
  filter.tag = null;
  filter.star = false;
  filter.time = false;
}

function genNotebooksBtnListener(notebookBtn, isCoNotebook) {
  notebookBtn.addEventListener("click", () => {
    filterInit();
    const lastReadNotebookId = localStorage.getItem("notebookId");
    const currentNotebookId = notebookBtn.dataset.notebookId;
    const noteBox = document.querySelector("#boxBtn");
    const tagCtn = document.querySelector(".tagCtn");
    let path = "";
    tagCtn.classList.add("display-none");
    noteBox.classList.add("selected");
    if (lastReadNotebookId === "null") {
      notebookBtn.classList.add("selected");
      localStorage.setItem("notebookId", currentNotebookId);
      if (isCoNotebook) {
        path = `/api/notebooks/${currentNotebookId}/notes?offset=0&limit=20&collaborators=1`;
      } else {
        path = `/api/notebooks/${currentNotebookId}/notes?offset=0&limit=20`;
      }
      genNotesCardCtn(
        notebookBtn.dataset.name,
        notebookBtn.dataset.notebookId,
        notebookBtn.dataset.description,
        path
      );
      genNotebookTagItems(notebookBtn.dataset.notebookId);
      return;
    }
    let lastSelectedNotebookBtn = document.querySelector(
      `.notebook[data-notebook-id = '${lastReadNotebookId}'`
    );

    if (!lastSelectedNotebookBtn) {
      localStorage.setItem("notebookId", currentNotebookId);
      genNotesCardCtn(
        notebookBtn.dataset.name,
        notebookBtn.dataset.notebookId,
        notebookBtn.dataset.description,
        path
      );
      genNotebookTagItems(notebookBtn.dataset.notebookId);
      return;
    }
    lastSelectedNotebookBtn.classList.remove("selected");
    notebookBtn.classList.add("selected");
    localStorage.setItem("notebookId", currentNotebookId);
    if (isCoNotebook) {
      path = `/api/notebooks/${currentNotebookId}/notes?offset=0&limit=20&collaborators=1`;
    } else {
      path = `/api/notebooks/${currentNotebookId}/notes?offset=0&limit=20`;
    }

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
  console.log(`notebook tag = ${tagDatas}`);
  const tagCtn = document.querySelector(".tagList");
  tagCtn.innerHTML = "";
  for (let tagData of tagDatas.tag) {
    const tagBtn = genTagItemBtn(tagData.name, tagData.tagId);
    tagCtn.appendChild(tagBtn);
  }
}

function genTagItemBtn(name, tagId) {
  const tagItem = document.createElement("div");
  const tagBtn = document.createElement("p");
  const trashBtn = document.createElement("img");
  tagItem.classList.add("tagItem");
  tagItem.classList.add("flex");
  tagBtn.textContent = name;
  trashBtn.src = "/static/resource/images/trash-white.png";
  trashBtn.dataset.tagName = name;
  trashBtn.dataset.tagid = tagId;

  const tagCtn = document.querySelector(".tagCtn");
  const displayTagCtnBtn = document.querySelector("#tagBtn");
  const notebookId = localStorage.getItem("notebookId");

  tagBtn.addEventListener("click", (e) => {
    e.stopPropagation();
    tagCtn.classList.add("display-none");
    const lastSelectBtn = document.querySelector(".tagItem p.selected");
    if (lastSelectBtn) {
      lastSelectBtn.classList.remove("selected");
      console.log(lastSelectBtn === tagBtn);
      if (lastSelectBtn === tagBtn) {
        filter.tag = null;
        tagBtn.classList.remove("selected");
        setNoteCardCtnByFilter();
        return;
      }
    }
    // displayTagCtnBtn.classList.add("selected");
    if (tagBtn.classList.contains("selected")) {
      displayTagCtnBtn.classList.remove("selected");
      filter.tag = null;
      tagBtn.classList.remove("selected");
    } else {
      filter.tag = name;
      filter.noteBox = false;
      tagBtn.classList.add("selected");
    }
    setNoteCardCtnByFilter();
  });

  trashBtn.addEventListener("click", async () => {
    const userCheck = confirm("Delete this tag?");
    if (!userCheck) {
      return;
    }
    tagCtn.classList.add("display-none");
    const path = `/api/notebooks/${notebookId}/tags?tag=${name}`;
    const result = await fetchData(path, "DELETE");
    console.log(result);
    if (result.result) {
      tagItem.remove();
      if (tagBtn.classList.contains("selected")) {
        filter.tag = null;
        filter.noteBox = true;
        filter.star = false;
        filter.time = false;
        filter.keyword = false;
        setNoteCardCtnByFilter();
      }
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
