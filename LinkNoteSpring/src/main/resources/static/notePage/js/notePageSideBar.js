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
  console.log(noteData);
  noteData.notes.forEach((note) => {
    createNoteTitle(note.star, note.name, note.select, note.noteId);
    //已經製作玩noteBtn，接著要監聽
  });
}

function createNoteTitle(star, name, select, noteId) {
  const notesGroup = document.querySelector(".notes-group");
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
  addNoteBtnListener(noteItem);
  notesGroup.appendChild(noteItem);
}

//傳遞note物件
function addNoteBtnListener(note) {
  note.addEventListener("click", async () => {
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
      } else {
        starBtn.setAttribute("src", "/static/resource/images/star-empty.png");
      }
      if (data.shared) {
        lockBtn.setAttribute("src", "/static/resource/images/lock.png");
      } else {
        lockBtn.setAttribute("src", "/static/resource/images/unlock.png");
      }
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
    createNoteTitle(false, "new note", true, result.noteId);
  });
}

notePageSideBarinit();
