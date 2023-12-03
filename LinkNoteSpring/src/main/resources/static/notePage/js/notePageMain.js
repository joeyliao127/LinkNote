async function notePageMainInit() {
  showTagsBtnListener();
  setTags();
  updateNoteTagListener();
  addTagBtnListener();
  saveNoteBtnListener();
  clearNoteTagsBtnListener();
  updateStarBtnListener();
}

function showTagsBtnListener() {
  const tagBtn = document.querySelectorAll(".tagBtn");
  const tagList = document.querySelectorAll(".tagListCtn");
  for (let i = 0; i < tagBtn.length; i++) {
    tagBtn[i].addEventListener("click", () => {
      tagList[i].classList.toggle("display-none");
    });
  }
}

function updateNoteTagListener() {
  const doneBtn = document.querySelector("#done");
  doneBtn.addEventListener("click", async () => {
    const updateList = [];
    const tagList = document.querySelectorAll(".tagItem");
    tagList.forEach((tag) => {
      if (tag.classList.contains("selected")) {
        updateList.push(tag.querySelector("p").textContent);
      }
    });
    console.log(updateList);
    const path = `/api/notebooks/${notebookId}/notes/${noteId}/tags`;
    const result = await fetchData(path, "PUT", { tag: updateList });
    if (result.result) {
      const noteTagsCtn = document.querySelector(".noteTagsCtn");
      noteTagsCtn.classList.toggle("display-none");
      MsgMaker.success("update tag success!");
    } else {
      MsgMaker.error("update tag faild");
    }
  });
}

async function setTags() {
  const path = `/api/notebooks/${notebookId}/tags`;
  const data = await fetchData(path, "GET");
  const notebookTagList = document.querySelector(".notebookTagsCtn .tagList");
  const noteTagList = document.querySelector(".noteTagsCtn .tagList");
  for (let j = 0; j < data.tag.length; j++) {
    const noteTag = genNoteTags(data.tag[j]);
    const notebookTag = genNotebookTags(data.tag[j]);
    notebookTagList.appendChild(notebookTag);
    noteTagList.appendChild(noteTag);
  }
}

function genNoteTags(tagName) {
  const tagDiv = document.createElement("div");
  const tag = document.createElement("p");
  tagDiv.classList.add("tagItem");
  tag.textContent = tagName;
  tagDiv.appendChild(tag);
  tagDiv.addEventListener("click", () => {
    tagDiv.classList.toggle("selected");
  });
  return tagDiv;
}

function genNotebookTags(tagName) {
  const tagDiv = document.createElement("div");
  const tag = document.createElement("p");
  const trash = document.createElement("img");
  tagDiv.classList.add("flex");
  tagDiv.classList.add("tagItem");
  tag.textContent = tagName;
  trash.src = "/static/resource/images/trash-white.png";
  trash.dataset.tag = tagName;
  tagDiv.appendChild(tag);
  tagDiv.appendChild(trash);
  tagDiv.addEventListener("click", () => {
    const tagList = document.querySelector(".notebookTagsCtn");
    tagList.classList.add("display-none");
  });
  return tagDiv;
}

//新增筆記本標籤
function addTagBtnListener() {
  const addTagBtn = document.querySelector("#addTag");
  addTagBtn.addEventListener("click", async () => {
    const input = document.querySelector("#createTag").value;
    console.log(input);
    const path = `/api/notebooks/${notebookId}/tags`;
    const result = await fetchData(path, "POST", { tag: input });
    console.log(result);
    if (result.result) {
      const notebookTagsCtn = document.querySelector(".notebookTagsCtn");
      const notebookTagList = notebookTagsCtn.querySelector(".tagList");
      const noteTagList = document.querySelector(".noteTagsCtn .tagList");
      notebookTagList.appendChild(genNotebookTags(input));
      noteTagList.appendChild(genNoteTags(input));
      notebookTagsCtn.classList.toggle("display-none");
      MsgMaker.success("Create new tag success");
    } else if (result.msg === "重複的資料") {
      MsgMaker.error("tag already exist.");
    } else {
      MsgMaker.error("Create new tag failed");
    }
  });
}

function saveNoteBtnListener() {
  const saveBtn = document.querySelector("#save");
  saveBtn.addEventListener("click", saveNoteContent);
}

async function saveNoteContent() {
  const name = document.querySelector("#noteName").value;
  if (name === "" || name.trim() === "" || name === null) {
    MsgMaker.error("note name is null");
    return;
  }
  const noteId = document.querySelector("#noteName").dataset.noteId;
  const question = document.querySelector("#question").value;
  const content = document.querySelector("#noteContent").value;
  const keypoint = document.querySelector("#keypoint").value;
  const sharedPermission =
    document.querySelector("#lockBtn img").dataset.shared;
  const body = {
    name,
    question,
    content,
    keypoint,
    sharedPermission,
  };
  const path = `/api/notebooks/${notebookId}/notes/${noteId}`;
  const result = await fetchData(path, "PUT", body);
  if (result.result) {
    MsgMaker.success("save note success");
  } else {
    MsgMaker.error("save note failed");
  }
}

function clearNoteTagsBtnListener() {
  const clearBtn = document.querySelector("#clear");
  clearBtn.addEventListener("click", () => {
    const tagItems = document.querySelectorAll(".noteTagsCtn .tagItem");
    tagItems.forEach((tag) => {
      tag.classList.remove("selected");
    });
  });
}

function updateStarBtnListener() {
  const starBtn = document.querySelector("#starBtn");
  starBtn.addEventListener("click", async () => {
    let star = starBtn.dataset.star;
    const noteId = document.querySelector("#noteName").dataset.noteId;
    const path = `/api/notebooks/${notebookId}/notes/${noteId}/star`;
    const starImg = starBtn.querySelector("img");
    console.log(`更改前的star:${star}`);
    if (star === "true") {
      console.log(`變更star為false`);
      star = false;
      starImg.setAttribute("src", "/static/resource/images/star-empty.png");
      starBtn.dataset.star = false;
    } else {
      console.log(`變更star為true`);
      star = true;
      starImg.setAttribute("src", "/static/resource/images/star-full.png");
      starBtn.dataset.star = true;
    }
    console.log(`最終更新的狀態${star}`);
    const result = await fetchData(path, "PUT", { star });
    if (result.result) {
      MsgMaker.success("Updated!");
    } else {
      MsgMaker.error("update failed");
    }
  });
}
notePageMainInit();
