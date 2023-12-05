const url = window.location.href.split("/");
const notebookId = url[url.length - 3];
let URL_noteId = url[url.length - 1];
let noteDataMap = {};
//noteDataMap用來儲存每一個讀取過的筆記，使用者在切換筆記時就不用重新fetch資料。

async function notePageSideBarInit() {
  createNewNoteBtnListener();
  genCollaboratorsList();
  await setNoteBtn(await getNotesData(0));
  displayFirstNoteWhenReflash();
  createCollaboratorListener();
}

function displayFirstNoteWhenReflash() {
  const firstNoteBtn = document.querySelector(".note-item");
  if (firstNoteBtn === null) {
    displayNoneEditArea();
    return;
  }
  const noteId = firstNoteBtn.dataset.noteId;
  removehHighlightNoteBtn();
  flag = noteId;
  firstNoteBtn.classList.add("selected");
  setNoteContent(noteId);
  setNoteTags(noteId);
}

function displayNoneEditArea() {
  const editArea = document.querySelector(".edit-area");
  const editChild = editArea.querySelectorAll("*");
  editChild.forEach((chlid) => {
    chlid.classList.add("display-none");
  });
}

function displayEditArea() {
  const editArea = document.querySelector(".edit-area");
  const editChild = editArea.querySelectorAll("*");
  editChild.forEach((chlid) => {
    chlid.classList.remove("display-none");
  });
}

async function getNotesData(offset) {
  const path = `/api/notebooks/${notebookId}/notes?offset=${offset}&limit=20`;
  return await fetchData(path, "GET");
}

//Controller
async function setNoteBtn(noteDataList) {
  noteDataList = noteDataList.notes;
  const notesGroup = document.querySelector(".notes-group");
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

//flag存取被highlight的noteBtn
let flag = -1;
function removehHighlightNoteBtn() {
  const noteList = document.querySelectorAll(".note-item");
  noteList.forEach((item) => {
    if (item.dataset.noteId === flag) {
      item.classList.toggle("selected");
    }
  });
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
    note.classList.toggle("selected");
    flag = note.dataset.noteId;
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
    removehHighlightNoteBtn();
    const noteItem = createNoteTitle(false, "new note", false, result.noteId);
    flag = result.noteId;
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
    const starCtn = document.querySelector("#starBtn ");
    const starBtn = document.querySelector("#starBtn img");
    const lockCtn = document.querySelector("#lockBtn");
    const lockBtn = document.querySelector("#lockBtn img");
    const date = document.querySelector(".date");
    const data = noteData.notePO;
    noteName.value = data.name;
    noteName.dataset.noteId = data.noteId;
    noteQuestion.value = data.question;
    noteContent.value = data.noteContent;
    noteKeypoint.value = data.keypoint;
    lockCtn.dataset.shared = data.shared;
    starCtn.dataset.star = data.star;
    date.textContent = data.createDate.split(" ")[0];
    if (data.star) {
      starBtn.setAttribute("src", "/static/resource/images/star-full.png");
    } else {
      starBtn.setAttribute("src", "/static/resource/images/star-empty.png");
    }
    if (data.shared) {
      lockBtn.setAttribute("src", "/static/resource/images/unlock.png");
    } else {
      lockBtn.setAttribute("src", "/static/resource/images/lock.png");
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
  const { tags } = noteTagsList;
  if (tags === undefined) {
    return;
  }
  //tag部分
  const tagItems = document.querySelectorAll(".noteTagList .tagItem");
  tagItems.forEach((tagItem) => {
    const notebookTag = tagItem.querySelector("p").textContent;
    //如果tag名稱存在於fetch中的資料，則加上selected。
    for (noteTag of tags) {
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
