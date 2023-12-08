const url = window.location.href.split("/");
const notebookId = url[url.length - 3];
let URL_noteId = url[url.length - 1];
let noteDataMap = {};
let filter = {
  noteBox: true,
  star: false,
  tag: null,
};
//noteDataMap用來儲存每一個讀取過的筆記，使用者在切換筆記時就不用重新fetch資料。

async function notePageSideBarInit() {
  setNotebookName();
  createNewNoteBtnListener();
  noteBoxBtnListener();
  notebookStarBtnListener();
  genCollaboratorsList();
  document.querySelector("#noteBoxBtn img").classList.add("selected");
  setNoteBtn(await getNotesData(filter));
  displayLastReadtNote();
  createCollaboratorListener();
}

function setNotebookName() {
  document.querySelector("#notebookName").textContent =
    localStorage.getItem("notebookName");
}

//displayLastReadtNote用於重新整理和刪除note之後使用
function displayLastReadtNote() {
  let noteId = localStorage.getItem("noteId");
  if (noteId === null || noteId === "null") {
    const firstNoteBtn = document.querySelector(".note-item");
    if (firstNoteBtn === null) {
      displayNoneEditArea(true);
      return;
    } else {
      displayNoneEditArea(false);
    }
    noteId = firstNoteBtn.dataset.noteId;
    localStorage.setItem("noteId", noteId);
  }
  const noteBtn = document.querySelector(
    `.note-item[data-note-id='${noteId}']`
  );
  if (!noteBtn) {
    return;
  }
  history.pushState({ noteId }, "", `/notebooks/${notebookId}/notes/${noteId}`);
  // removehHighlightNoteBtn();
  noteBtn.classList.add("selected");
  setNoteContent(noteId);
  setNoteTags(noteId);
}

function displayFirstNote() {
  localStorage.setItem("noteId", null);
  displayLastReadtNote();
}

function displayNoneEditArea(display) {
  const editChilds = document.querySelector(".edit-area").querySelectorAll("*");

  if (display) {
    editChilds.forEach((chlid) => {
      chlid.classList.add("display-none");
    });
  } else {
    editChilds.forEach((chlid) => {
      chlid.classList.remove("display-none");
    });
    document.querySelector(".noteTagsCtn").classList.add("display-none");
  }
}

function displayEditArea() {
  const editArea = document.querySelector(".edit-area");
  const editChild = editArea.querySelectorAll("*");
  editChild.forEach((chlid) => {
    chlid.classList.remove("display-none");
  });
}

//使用方式：將filter狀態先寫好，再來呼叫此函式，透過filter變換btn select
async function filterBtHighLightSwitcher() {
  const noteBoxBtn = document.querySelector("#noteBoxBtn img");
  const starBtn = document.querySelector("#notebookStarBtn img");
  const tagBtn = document.querySelector("#notebookTagBtn");

  if (filter.noteBox) {
    noteBoxBtn.classList.add("selected");
    starBtn.classList.remove("selected");
    starStatus = true;
    tagBtn.classList.remove("selected");
  } else if (filter.star && filter.tag) {
    starBtn.classList.add("selected");
    tagBtn.classList.add("selected");
    noteBoxBtn.classList.remove("selected");
  } else if (filter.star && !filter.tag) {
    noteBoxBtn.classList.remove("selected");
    tagBtn.classList.remove("selected");
    starBtn.classList.add("selected");
  } else if (filter.tag && !filter.star) {
    noteBoxBtn.classList.remove("selected");
    starBtn.classList.remove("selected");
    tagBtn.classList.add("selected");
  } else if (!filter.star && !filter.tag) {
    noteBoxBtn.classList.add("selected");
    starBtn.classList.remove("selected");
    starStatus = true;
    tagBtn.classList.remove("selected");
  }
  setNoteBtn(await getNotesData(filter));
  displayFirstNote();
}

function noteBoxBtnListener() {
  const noteBoxBtn = document.querySelector("#noteBoxBtn");
  noteBoxBtn.addEventListener("click", async () => {
    if (filter.noteBox) {
      return;
    }
    filter.noteBox = true;
    filter.star = false;
    filter.tag = false;

    filterBtHighLightSwitcher();
  });
}

//true是代表star selected
let starStatus = true;
function notebookStarBtnListener() {
  const starBtn = document.querySelector("#notebookStarBtn");
  starBtn.addEventListener("click", async () => {
    if (starStatus) {
      filter.noteBox = false;
      filter.star = true;
      starStatus = false;
    } else {
      filter.star = false;
      starStatus = true;
    }

    filterBtHighLightSwitcher();
  });
}

async function getNotesData(filter) {
  let path = `/api/notebooks/${notebookId}/notes?offset=0&limit=20`;
  if (filter.noteBox) {
    return await fetchData(path, "GET");
  }
  if (filter.star) {
    path += `&star=1`;
  }
  if (filter.tag) {
    path += `&tag=${filter.tag}`;
  }

  return await fetchData(path, "GET");
}

//Controller
function setNoteBtn(noteDataList) {
  noteDataList = noteDataList.notes.notes;
  const notesGroup = document.querySelector(".notes-group");
  while (notesGroup.firstChild) {
    notesGroup.removeChild(notesGroup.firstChild);
  }
  noteDataList.forEach((note) => {
    const noteItem = createNoteTitle(
      note.star,
      note.name,
      note.select,
      note.noteId
    );
    setNoteBtnListener(noteItem);
    notesGroup.appendChild(noteItem);
  });
}

//createNoteTitle除了設定noteBtn的名稱，順便將note的id也記在上面
function createNoteTitle(star, name, select, noteId) {
  const noteItem = document.createElement("div");
  const noteTitle = document.createElement("p");
  noteItem.classList.add("note-item");
  noteItem.classList.add("flex");
  noteItem.dataset.noteId = noteId;
  noteTitle.textContent = name;
  noteItem.appendChild(noteTitle);
  if (select) {
    noteItem.classList.add("selected");
  }
  if (star) {
    const img = document.createElement("img");
    img.setAttribute("src", "/static/resource/images/star-full.png");
    noteItem.appendChild(img);
  }
  return noteItem;
}

function removehHighlightNoteBtn() {
  const noteId = localStorage.getItem("noteId");
  const noteBtn = document.querySelector(
    `.note-item[data-note-id='${noteId}']`
  );

  //如果一個筆記都沒有，則選不到任何btn，因此要設定此行
  if (noteBtn === null) {
    return;
  }
  noteBtn.classList.remove("selected");
}
//setNoteBtnListener輸入參數為noteBtn物件
function setNoteBtnListener(note) {
  note.addEventListener("click", async () => {
    const editArea = document.querySelector(".edit-area");
    editArea.querySelectorAll("*").forEach((item) => {
      item.classList.remove("display-none");
      document
        .querySelector(".noteTagsCtn")
        .setAttribute("class", "tagListCtn noteTagsCtn flex display-none");
    });

    removehHighlightNoteBtn();
    note.classList.add("selected");
    localStorage.setItem("noteId", note.dataset.noteId);
    const noteId = note.dataset.noteId;
    setNoteContent(noteId);
    setNoteTags(noteId);
  });
}

function createNewNoteBtnListener() {
  const createNoteBtn = document.querySelector("#newNoteBtn");
  createNoteBtn.addEventListener("click", async () => {
    const path = `/api/notebooks/${notebookId}/notes`;
    const result = await fetchData(path, "POST");
    const notesGroup = document.querySelector(".notes-group");
    const noteItem = createNoteTitle(false, "new note", false, result.noteId);
    notesGroup.appendChild(noteItem);
    setNoteBtnListener(noteItem);
  });
}

async function getNoteContent(noteId) {
  const notePath = `/api/notebooks/${notebookId}/notes/${noteId}`;
  const content = await fetchData(notePath, "GET");
  return content;
}

async function getNoteTags(noteId) {
  const tagPath = `/api/notebooks/${notebookId}/notes/${noteId}/tags`;
  const tags = await fetchData(tagPath, "GET");
  return tags;
}

async function setNoteContent(noteId) {
  const noteData = await getNoteContent(noteId);
  if (noteData.result) {
    const noteName = document.querySelector("#noteName");
    const noteQuestion = document.querySelector("#question");
    const noteContent = document.querySelector("#noteContent");
    const noteKeypoint = document.querySelector("#keypoint");
    const starCtn = document.querySelector("#starBtn");
    const starBtn = document.querySelector("#starBtn img");
    const date = document.querySelector(".date");
    const data = noteData.notePO;
    noteName.value = data.name;
    noteName.dataset.noteId = data.noteId;
    noteQuestion.value = data.question;
    noteContent.value = data.noteContent;
    noteKeypoint.value = data.keypoint;
    starCtn.dataset.star = data.star;
    date.textContent = data.createDate.split(" ")[0];
    if (data.star) {
      starBtn.setAttribute("src", "/static/resource/images/star-full.png");
    } else {
      starBtn.setAttribute("src", "/static/resource/images/star-empty.png");
    }

    history.pushState(
      { noteId },
      "",
      `/notebooks/${notebookId}/notes/${noteId}`
    );
    URL_noteId = noteId;
  } else {
    MsgMaker.error("error..");
    return;
  }
}

async function setNoteTags(noteId) {
  const noteTagsList = await getNoteTags(noteId);
  const { tag } = noteTagsList;

  //tag部分
  const tagItems = document.querySelectorAll(".noteTagList .tagItem");
  tagItems.forEach((tagItem) => {
    tagItem.classList.remove("selected");
    const notebookTag = tagItem.querySelector("p").textContent;
    //如果tag名稱存在於fetch中的資料，則加上selected。
    for (noteTag of tag) {
      if (noteTag.name === notebookTag) {
        tagItem.classList.add("selected");
      }
    }
  });
}

async function createCollaboratorListener() {
  const createCtn = document.querySelector(".inviteInput");
  const createBtn = createCtn.querySelector("img");
  createBtn.addEventListener("click", async () => {
    const checkCollaboratorsCount = document.querySelectorAll(".editor-item");
    if (checkCollaboratorsCount.length >= 5) {
      MsgMaker.error("collaborator count are limit.");
      return;
    }
    const email = createCtn.querySelector("input").value;
    createCtn.querySelector("input").value = "";
    if (!verifyEmailRegx(email)) {
      MsgMaker.error("incorrect email format");
      return;
    }
    const path = `/api/notebooks/${notebookId}/collaborators`;
    const result = await fetchData(path, "POST", { email });
    if (result.result) {
      const collaboratorsCtn = document.querySelector(".sideBar-ctn-editors");
      const { collaborator } = result;
      const coEditor = genCollaborators(
        collaborator.username,
        collaborator.userId
      );
      collaboratorsCtn.appendChild(coEditor);
      MsgMaker.success(`invite user success`);
    } else if (result.msg === "重複的資料") {
      MsgMaker.error(` email address already exist`);
    } else {
      MsgMaker.error(`invalid email address`);
    }
  });
}

async function genCollaboratorsList() {
  const collaboratorsCtn = document.querySelector(".sideBar-ctn-editors");
  const owner = collaboratorsCtn.querySelector("#owner p");

  const ownerInfo = await fetchData(`/api/user`, "GET");
  owner.textContent = ownerInfo.username;
  const path = `/api/notebooks/${notebookId}/collaborators`;
  const collaborators = await fetchData(path, "GET");
  collaborators.collaborators.forEach((collaborator) => {
    const coEditor = genCollaborators(
      collaborator.username,
      collaborator.userId
    );
    collaboratorsCtn.appendChild(coEditor);
  });
}

function genCollaborators(username, userId) {
  const usernameText = document.createElement("p");
  const coEditor = document.createElement("div");
  const trashBtn = document.createElement("img");
  usernameText.textContent = username;
  trashBtn.src = "/static/resource/images/close.png";
  coEditor.classList.add("editor-item");
  coEditor.classList.add("flex");
  coEditor.dataset.userId = userId;
  coEditor.appendChild(usernameText);
  coEditor.appendChild(trashBtn);

  trashBtn.addEventListener("click", async () => {
    if (!window.confirm("Remove this collaborator?")) {
      return;
    }
    const result = await deleteCOllaborators(userId);
    if (result.result) {
      coEditor.remove();
      MsgMaker.success("Delete collaborators success.");
    } else {
      MsgMaker.error("Delete collaborators failed");
    }
  });
  return coEditor;
}

async function deleteCOllaborators(userId) {
  const path = `/api/notebooks/1/collaborators/${userId}`;
  return await fetchData(path, "DELETE");
}

notePageSideBarInit();
