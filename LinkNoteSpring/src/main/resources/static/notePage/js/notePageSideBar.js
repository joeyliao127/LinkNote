const urlSearchParams = new URLSearchParams(window.location.search);
const params = Object.fromEntries(urlSearchParams.entries());
const notebookId = params.notebook;
const noteId = params.note;
console.log("notebookId:", notebookId);
console.log("noteId", noteId);

function notePageSideBarinit() {
  createNote();
}

function createNoteTitle(notesGroup, star, title, select, noteId) {
  const noteItem = document.createElement("div");
  const noteTitle = document.createElement("p");
  noteItem.classList.add("note-item");
  noteItem.classList.add("flex");
  noteItem.setAttribute("data-noteId", noteId);
  noteTitle.textContent = title;
  noteItem.appendChild(noteTitle);
  if (select) {
    noteItem.classList.add("selected");
  }
  if (star) {
    const img = document.createElement("img");
    img.setAttribute("src", "/static/resource/images/star-full.png");
    noteItem.appendChild(img);
  }
  notesGroup.appendChild(noteItem);
}

function createNote() {
  const createNoteBtn = document.querySelector("#newNoteBtn");
  createNoteBtn.addEventListener("click", async () => {
    const noteGroup = document.querySelector(".notes-group");
    console.log(noteGroup);
    const path = `/api/notebooks/${notebookId}/notes`;
    const result = await fetchData(path, "POST");
    createNoteTitle(noteGroup, false, "new note", true, result.noteId);
  });
}

notePageSideBarinit();
