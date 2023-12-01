const urlSearchParams = new URLSearchParams(window.location.search);
const params = Object.fromEntries(urlSearchParams.entries());
const notebookId = params.notebook;
const noteId = params.note;
console.log("notebookId:", notebookId);
console.log("noteId", noteId);

function notePageSideBarinit() {
  createNote();
}

function createNote() {
  const createNoteBtn = document.querySelector("#newNoteBtn");
  createNoteBtn.addEventListener("click", async () => {
    const noteGroup = document.querySelector(".notes-group");
    console.log(noteGroup);
    const path = `/api/notebooks/${notebookId}/notes`;
    const result = await fetchData(path, "POST");
    window.location.href = `/notePage.html?notebook=${notebookId}&note=${result.noteId}`;
    createNoteTitle(noteGroup, false, "new note", true, noteId.noteId);
  });
}
notePageSideBarinit();
