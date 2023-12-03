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
  console.log(`取得所有note`);
  console.log(noteList);
  console.log(`取消的noteId = ${flag}`);
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
    const path = `/api/notebooks/${notebookId}/notes/${noteId}`;
    console.log(`path = ${path}`);
    const noteData = await fetchData(path, "GET");
    if (noteData.result) {
      const noteName = document.querySelector("#noteName");
      const noteQuestion = document.querySelector("#question");
      const noteContent = document.querySelector("#noteContent");
      const noteKeypoint = document.querySelector("#keypoint");
      const starBtn = document.querySelector("#starBtn");
      const lockBtn = document.querySelector("#lockBtn");
      const date = document.querySelector(".date");
      const data = noteData.notePO;
      noteName.value = data.name;
      noteQuestion.value = data.question;
      noteContent.value = data.noteContent;
      noteKeypoint.value = data.keypoint;
      date.textContent = data.createDate.split(" ")[0];
      if (data.star) {
        starBtn.setAttribute("src", "/static/resource/images/star-full.png");
      }
      if (data.shared) {
        lockBtn.setAttribute("src", "/static/resource/images/lock.png");
      } else {
        lockBtn.setAttribute("src", "/static/resource/images/unlock.png");
      }
      history.pushState(
        { noteId },
        "",
        `/notebooks/${notebookId}/notes/${noteId}`
      );
    } else {
      MsgMaker.error("error..");
      return;
    }
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
