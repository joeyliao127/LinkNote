function userSpaceNoteConsoleInit() {
  createNewNoteBtnListener();
  noteToolBtnsListener();
}

function createNewNoteBtnListener() {
  const newNoteBtn = document.querySelector(".newNoteBtn");
  newNoteBtn.addEventListener("click", async () => {
    const notebook = document.querySelector(".main-group-subjectInfo h2");
    localStorage.setItem("notebookName", notebook.textContent);
    const notebookId = notebook.dataset.notebookId;
    const path = `/api/notebooks/${notebookId}/notes`;
    const result = await fetchData(path, "POST");
    if (result.result) {
      localStorage.setItem("noteId", result.noteId);
      window.location.href = `/notebooks/${notebookId}/notes/${result.noteId}`;
    } else {
      MsgMaker.error("Create note failed");
    }
  });
}

let filter = {
  noteBox: true,
  tag: false,
  tagName: null,
  time: false,
  star: false,
  keyword: null,
};
function noteToolBtnsListener() {
  displayTagListBtnListener();
  deleteNotebookBtnListener();
  noteSortByTimeBtnListener();
  setNoteStarBtnListner();
  searchNoteByKeywordListener();
}

function displayTagListBtnListener() {
  const tagBtn = document.querySelector("#tagBtn");
  tagBtn.addEventListener("click", () => {
    document.querySelector(".tagList").classList.toggle("display-none");
  });
}
function noteSortByTimeBtnListener() {}

function setNoteStarBtnListner() {}

function searchNoteByKeywordListener() {}

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
