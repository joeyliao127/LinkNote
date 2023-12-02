function userSpaceNoteConsoleInit() {
  noteCardLitener();
  createNoteBtnListener();
  deleteNotebookBtnListener();
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

function deleteNotebookBtnListener() {
  const delBtn = document.querySelector("#delBtn");
  delBtn.addEventListener("click", async () => {
    const notebookName = document.querySelector(".main-group-subjectInfo h2");
    const notebookId = notebookName.dataset.notebookId;
    if (notebookId === undefined) {
      MsgMaker.warn("please select your notebook first");
      return;
    }
    const action = window.confirm("Are you sure delete this notebook?");
    if (action) {
      const path = `/api/notebooks/${notebookId}`;
      const result = await fetchData(path, "DELETE");
      console.log(result);
      if (result.result) {
        const noteCardsCtn = document.querySelector(".main-group-notes");
        const description = document.querySelector(".main-group-subjectInfo p");
        notebookName.textContent = "Select your notebook";
        noteCardsCtn.innerHTML = "";
        description.textContent = "";
        removeNotebookBtn(notebookId);
        MsgMaker.success("deleted notebook success.");
      } else {
        MsgMaker.error("deleted notebook failed");
      }
    } else {
      return;
    }
  });
}
userSpaceNoteConsoleInit();
