const url = window.location.href.split("/");
const notebookId = url[url.length - 3];
let URL_noteId = url[url.length - 1];
console.log(URL_noteId);
let noteDataMap = {};
//noteDataMap用來儲存每一個讀取過的筆記，使用者在切換筆記時就不用重新fetch資料。

async function notePageSideBarInit() {
  createNewNoteBtnListener();
  await setNoteBtn(await getNotesData(0));
  displayFirstNoteWhenReflash();
  tagListMouseLeftListener();
}

function displayFirstNoteWhenReflash() {
  const firstNoteBtn = document.querySelector(".note-item");
  console.log(firstNoteBtn);
  const noteId = firstNoteBtn.dataset.noteId;
  removehHighlightNoteBtn();
  flag = noteId;
  firstNoteBtn.classList.add("selected");
  console.log(noteId);
  setNoteContent(noteId);
  setNoteTags(noteId);
}

function tagListMouseLeftListener() {
  const tagListCtns = document.querySelectorAll(".tagListCtn");
  tagListCtns.forEach((tagListCtn) => {
    tagListCtn.addEventListener("mouseleave", () => {
      tagListCtn.classList.toggle("display-none");
    });
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
  note.addEventListener("click", async (e) => {
    e.stopPropagation();
    removehHighlightNoteBtn();
    note.classList.toggle("selected");
    flag = note.dataset.noteId;
    const noteId = note.dataset.noteId;
    console.log(noteId);
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
  console.log(noteTagsList);
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

notePageSideBarInit();
