const notebookId = location.href.split("=")[1];
console.log("notebookId:", notebookId);
function notePageSideBarinit() {}

function createNote() {
  const createNotebtn = document.querySelector("#newNoteBtn");
  const notesGroup = document.querySelector(".notes-group");
  createNotebtn.addEventListener("click", async () => {
    const response = await fetch(`/api/notebook/${notebookId}/notes`);
    if (response.ok) {
    }
  });
}
