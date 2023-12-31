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
  noteBoxBtnListener();
  displayTagListBtnListener();
  deleteNotebookBtnListener();
  noteSortByTimeBtnListener();
  setNoteStarBtnListner();
  searchNoteByKeywordListener();
  createNotebookTagListener();
}

function createNotebookTagListener() {
  const btn = document.querySelector(".tagBtnGroup button");
  btn.addEventListener("click", async () => {
    const tagInput = document.querySelector("#createTag");
    const tag = tagInput.value;
    tagInput.vale = "";
    if (!tag.trim()) {
      return;
    }
    const notebookId = localStorage.getItem("notebookId");
    const path = `/api/notebooks/${notebookId}/tags`;
    console.log(path);
    const result = await fetchData(path, "POST", { tag });
    if (result.result) {
      document
        .querySelector(".tagList")
        .appendChild(genTagItemBtn(tag, result.tagId));
      document.querySelector(".tagCtn").classList.add("display-none");
      MsgMaker.success("create tag success!");
    } else if (result.msg === "重複的資料") {
      MsgMaker.error("tag already exist.");
    } else {
      Ms.error("create tag failed");
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
  const tagBtn = document.querySelector("#tagBtn");
  const timeBtn = document.querySelector("#timeBtn");
  const starBtn = document.querySelector("#starBtn");
  if (!notebookId) {
    MsgMaker.warn("select notebook first");
    return;
  }

  let path = `/api/notebooks/${notebookId}/notes?limit=20&offset=0`;

  if (filter.keyword) {
    path += `&keyword=${filter.keyword}`;
  }

  console.log(!filter.star && !filter.tag && !filter.time && !filter.keyword);
  if (
    filter.noteBox ||
    (!filter.star && !filter.tag && !filter.time && !filter.keyword)
  ) {
    console.log("執行生成note card", path);
    noteBoxBtn.classList.add("selected");
    tagBtn.classList.remove("selected");
    timeBtn.classList.remove("selected");
    starBtn.classList.remove("selected");
    genNotesCardCtn(notebookName, notebookId, description, path);
    return;
  }
  noteBoxBtn.classList.remove("selected");
  if (filter.star) {
    starBtn.classList.add("selected");
  } else {
    starBtn.classList.remove("selected");
  }
  if (filter.time) {
    timeBtn.classList.add("selected");
  } else {
    timeBtn.classList.remove("selected");
  }
  if (filter.tag) {
    tagBtn.classList.add("selected");
  } else {
    tagBtn.classList.remove("selected");
  }

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

function CheckIfNotebookIsSelected() {
  const notebookBtn = document.querySelector(".notebook.selected");
  if (!notebookBtn) {
    MsgMaker.warn("please select your notebook first");
    return false;
  }
  return true;
}

function noteBoxBtnListener() {
  console.log(`=======點擊boxBtn=======`);
  const noteBox = document.querySelector("#boxBtn");
  noteBox.addEventListener("click", () => {
    document.querySelector(".tagCtn").classList.add("display-none");
    if (!CheckIfNotebookIsSelected()) {
      return;
    }
    filterInit();
    setNoteCardCtnByFilter();
  });
}
document.querySelector(".tagCtn").classList.add("display-none");
function displayTagListBtnListener() {
  const tagBtn = document.querySelector("#tagBtn");
  const tagCtn = document.querySelector(".tagCtn");
  tagBtn.addEventListener("click", () => {
    console.log(`=======點擊tag=======`);
    if (!CheckIfNotebookIsSelected()) {
      return;
    }
    tagCtn.classList.remove("display-none");

    console.log(`filter`);
    console.log(filter);
  });
}

function noteSortByTimeBtnListener() {
  const timeBtn = document.querySelector("#timeBtn");
  timeBtn.addEventListener("click", () => {
    if (!CheckIfNotebookIsSelected()) {
      return;
    }
    if (timeBtn.classList.contains("selected")) {
      filter.time = false;
    } else {
      filter.noteBox = false;
      filter.time = true;
    }
    setNoteCardCtnByFilter();
  });
}

function setNoteStarBtnListner() {
  const starBtn = document.querySelector("#starBtn");
  starBtn.addEventListener("click", () => {
    if (!CheckIfNotebookIsSelected()) {
      return;
    }
    if (starBtn.classList.contains("selected")) {
      filter.star = false;
    } else {
      filter.noteBox = false;
      filter.star = true;
    }
    setNoteCardCtnByFilter();
  });
}

function searchNoteByKeywordListener() {
  const searchBtn = document.querySelector("#searchBtn");
  searchBtn.addEventListener("click", () => {
    if (!CheckIfNotebookIsSelected()) {
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
    if (!CheckIfNotebookIsSelected()) {
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
