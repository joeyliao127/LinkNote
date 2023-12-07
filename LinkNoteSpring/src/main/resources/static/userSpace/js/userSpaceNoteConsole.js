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

function createNotebookTagListener() {
  const btn = document.querySelector(".tagBtnGroup button");
  btn.addEventListener(() => {
    const tag = document.querySelector("#createTag").value;
    if (!tag.trim()) {
      return;
    }
  });
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
  if (!notebookBtn) {
    MsgMaker.warn("please select your notebook first");
    return;
  }
  const notebookName = notebookBtn.dataset.name;
  const notebookId = notebookBtn.dataset.notebookId;
  const description = notebookBtn.dataset.description;
  const noteBoxBtn = document.querySelector("#boxBtn");
  if (!notebookId) {
    MsgMaker.warn("select notebook first");
    return;
  }

  let path = `/api/notebooks/${notebookId}/notes?limit=20&offset=0`;

  if (filter.keyword) {
    path += `&keyword=${filter.keyword}`;
  }

  console.log(`檢查一大串`);
  console.log(!filter.star && !filter.tag && !filter.time && !filter.keyword);
  if (
    filter.noteBox ||
    (!filter.star && !filter.tag && !filter.time && !filter.keyword)
  ) {
    console.log("執行生成note card", path);
    noteBoxBtn.classList.add("selected");
    genNotesCardCtn(notebookName, notebookId, description, path);
    return;
  }
  noteBoxBtn.classList.remove("selected");

  if (filter.star) {
    path += `&star=1`;
  }

  if (filter.time) {
    path += `&timeAsc=1`;
  }

  if (filter.tag) {
    path += `&tag=${filter.tag}`;
  }

  console.log(path);
  genNotesCardCtn(notebookName, notebookId, description, path);
}

function checkNotebookHasSelected() {
  const notebookBtn = document.querySelector(".notebook.selected");
  if (!notebookBtn) {
    MsgMaker.warn("please select your notebook first");
    return false;
  }
  return true;
}

function displayTagListBtnListener() {
  const tagBtn = document.querySelector("#tagBtn");
  const tagCtn = document.querySelector(".tagCtn");
  tagBtn.addEventListener("click", () => {
    console.log(`=======點擊tag=======`);
    if (!checkNotebookHasSelected()) {
      return;
    }

    tagCtn.classList.toggle("display-none");

    console.log(`filter`);
    console.log(filter);
  });
}

function noteSortByTimeBtnListener() {
  const timeBtn = document.querySelector("#timeBtn");
  timeBtn.addEventListener("click", () => {
    console.log(`=======點擊time=======`);
    if (!checkNotebookHasSelected()) {
      return;
    }
    if (timeBtn.classList.contains("selected")) {
      filter.time = false;
      timeBtn.classList.remove("selected");
    } else {
      filter.noteBox = false;
      filter.time = true;
      timeBtn.classList.add("selected");
    }
    console.log(`filter`);
    console.log(filter);
    setNoteCardCtnByFilter();
  });
}

function setNoteStarBtnListner() {
  const starBtn = document.querySelector("#starBtn");
  starBtn.addEventListener("click", () => {
    console.log(`=======點擊star=======`);
    if (!checkNotebookHasSelected()) {
      return;
    }
    if (starBtn.classList.contains("selected")) {
      filter.star = false;
      starBtn.classList.remove("selected");
    } else {
      filter.noteBox = false;
      filter.star = true;
      starBtn.classList.add("selected");
    }
    console.log(`filter`);
    console.log(filter);
    setNoteCardCtnByFilter();
  });
}

function searchNoteByKeywordListener() {
  const searchBtn = document.querySelector("#searchBtn");
  searchBtn.addEventListener("click", () => {
    if (!checkNotebookHasSelected()) {
      return;
    }
    const keyword = document.querySelector("#keyword").value;
    if (!keyword) {
      return;
    }
    filter.keyword = keyword;
    setNoteCardCtnByFilter();
  });
}

function deleteNotebookBtnListener() {
  const delBtn = document.querySelector("#delBtn");
  delBtn.addEventListener("click", async () => {
    if (!checkNotebookHasSelected()) {
      return;
    }
    const notebookName = document.querySelector(".main-group-subjectInfo h2");
    const notebookId = notebookName.dataset.notebookId;
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
