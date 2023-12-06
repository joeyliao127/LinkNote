function userSpaceNoteConsoleInit() {
  createNewNoteBtnListener();
  noteToolBtnsListener();
}

function createNewNoteBtnListener() {
  const newNoteBtn = document.querySelector(".newNoteBtn");
  newNoteBtn.addEventListener("click", async () => {
    const notebookBtn = document.querySelector(".notebook.selected");
    if (!notebookBtn) {
      MsgMaker.warn("selected notebook first.");
      return;
    }
    localStorage.setItem("notebookName", notebookBtn.textContent);
    const notebookId = notebookBtn.dataset.notebookId;
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

function noteToolBtnsListener() {
  displayTagListBtnListener();
  deleteNotebookBtnListener();
  noteSortByTimeBtnListener();
  setNoteStarBtnListner();
  searchNoteByKeywordListener();
}
let filter = {
  noteBox: true,
  star: false,
  time: false,
  tag: null,
  keyword: null,
};
async function setNoteCardCtnByFilter() {
  const notebookBtn = document.querySelector(".notebook.selected");
  const notebookName = notebookBtn.dataset.name;
  const notebookId = notebookBtn.dataset.notebookId;
  const description = notebookBtn.dataset.description;

  if (!notebookId) {
    MsgMaker.warn("select notebook first");
    return;
  }

  let path = `/api/notebooks/${notebookId}?`;
  if (filter.noteBox) {
    path = `/api/notebooks/${notebookId}`;
    genNotesCardCtn(notebookName, notebookId, description, path);
    return;
  }

  if (filter.star) {
    path += `star=1`;
  } else {
    path += `star=0`;
  }

  if (filter.time) {
    path += `&timeAsc=1`;
  }

  if (filter.tag) {
    path += `&tag=${filter.tag}`;
  }

  if (filter.keyword) {
    path += `&keyword=${filter.keyword}`;
  }
  console.log(path);
  genNotesCardCtn(notebookName, notebookId, description, path);
}

function displayTagListBtnListener() {
  const tagBtn = document.querySelector("#tagBtn");
  tagBtn.addEventListener("click", () => {
    document.querySelector(".tagList").classList.toggle("display-none");
  });
}

function noteSortByTimeBtnListener() {}

function setNoteStarBtnListner() {
  const starBtn = document.querySelector("#starBtn");
  starBtn.addEventListener("click", () => {
    setNoteCardCtnByFilter();
  });
}

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
