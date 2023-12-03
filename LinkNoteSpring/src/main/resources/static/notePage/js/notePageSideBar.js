const url = window.location.href.split("/");
const notebookId = url[url.length - 3];
const noteId = url[url.length - 1];

function notePageSideBarinit() {
  createNoteBtnListener();
  setAllNoteBtn();
}

let noteDataMap = {};

async function setAllNoteBtn() {
  const path = `/api/notebooks/${notebookId}/notes?limit=20`;
  //noteData is iterable
  const noteData = await fetchData(path, "GET");
  const notesGroup = document.querySelector(".notes-group");
  noteData.notes.forEach((note) => {
    const noteItem = createNoteTitle(
      note.star,
      note.name,
      note.select,
      note.noteId
    );
    notesGroup.appendChild(noteItem);
    addNoteBtnListener(noteItem);
  });
}

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

let flag = -1;

function removehHighlightNoteBtn() {
  const noteList = document.querySelectorAll(".note-item");
  noteList.forEach((item) => {
    if (item.dataset.noteId === flag) {
      item.classList.toggle("selected");
    }
  });
}
//傳遞note物件
function addNoteBtnListener(note) {
  note.addEventListener("click", async () => {
    removehHighlightNoteBtn();
    note.classList.toggle("selected");
    flag = note.dataset.noteId;
    const noteId = note.dataset.noteId;
    genNoteContent(noteId);
  });
}

function createNoteBtnListener() {
  const createNoteBtn = document.querySelector("#newNoteBtn");
  createNoteBtn.addEventListener("click", async () => {
    const path = `/api/notebooks/${notebookId}/notes`;
    const result = await fetchData(path, "POST");
    const notesGroup = document.querySelector(".notes-group");
    removehHighlightNoteBtn();
    const noteItem = createNoteTitle(false, "new note", false, result.noteId);
    flag = result.noteId;
    notesGroup.appendChild(noteItem);
    addNoteBtnListener(noteItem);
  });
}

notePageSideBarinit();

async function genNoteContent(noteId) {
  const notePath = `/api/notebooks/${notebookId}/notes/${noteId}`;
  const noteData = await fetchData(notePath, "GET");
  const tagPath = `/api/notebooks/${notebookId}/notes/${noteId}/tags`;
  const tagData = await fetchData(tagPath, "GET");
  if (noteData.result && tagData.result) {
    const noteName = document.querySelector("#noteName");
    const noteQuestion = document.querySelector("#question");
    const noteContent = document.querySelector("#noteContent");
    const noteKeypoint = document.querySelector("#keypoint");
    const starBtn = document.querySelector("#starBtn img");
    const lockBtn = document.querySelector("#lockBtn img");
    const date = document.querySelector(".date");
    const data = noteData.notePO;
    noteName.value = data.name;
    noteName.dataset.noteId = data.noteId;
    noteQuestion.value = data.question;
    noteContent.value = data.noteContent;
    noteKeypoint.value = data.keypoint;
    lockBtn.dataset.shared = data.shared;
    starBtn.dataset.star = data.star;
    console.log(starBtn);
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
    const noteTags = tagData.tag;
    //tag部分
    const tagItems = document.querySelectorAll(".noteTagList .tagItem");
    console.log(tagItems);
    tagItems.forEach((tagItem) => {
      const tag = tagItem.querySelector("p").textContent;
      if (tag in noteTags) {
        tagItem.classList.add("selected");
      }
    });
    history.pushState(
      { noteId },
      "",
      `/notebooks/${notebookId}/notes/${noteId}`
    );
  } else {
    MsgMaker.error("error..");
    return;
  }
}
