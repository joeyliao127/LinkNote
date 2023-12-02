function userSpaceNoteConsoleInit() {
  noteCardLitener();
  createNoteBtnListener();
}

function noteCardLitener() {
  const noteCardList = document.querySelectorAll(".main-item-noteCard");
  noteCardList.forEach((card) => {
    card.addEventListener("click", () => {
      window.location.href = "/notebooks/1/notes/1";
    });
  });
}

function createNoteBtnListener() {
  const newNoteBtn = document.querySelector(".newNoteBtn");
  newNoteBtn.addEventListener("click", async () => {
    const notebook = document.querySelector(".main-group-subjectInfo h2");
    console.log(notebook);
    const notebookId = notebook.dataset.notebookId;
    console.log(notebookId);

    const path = `/api/notebooks/${notebookId}/notes`;
    const result = await fetchData(path, "POST");
    if (result.result) {
      window.location.href = `/notebooks/${notebookId}/notes/${result.noteId}`;
    } else {
      MsgMaker.error("Create note failed");
    }
  });
}

userSpaceNoteConsoleInit();
