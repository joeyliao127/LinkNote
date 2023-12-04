async function notePageMainInit() {
  controller();
  displayTagsBtnListener();
  updateNoteTagListener();
  addTagBtnListener();
  saveNoteBtnListener();
  clearNoteTagsBtnListener();
  updateStarBtnListener();
}

function controller() {
  setTags();
}
//點選撰寫筆記區的tag按鈕後，顯示notebook所有標籤
function displayTagsBtnListener() {
  const tagBtn = document.querySelectorAll(".tagBtn");
  const tagList = document.querySelectorAll(".tagListCtn");
  for (let i = 0; i < tagBtn.length; i++) {
    tagBtn[i].addEventListener("click", () => {
      tagList[i].classList.toggle("display-none");
    });
  }
}

//render左邊tag和右邊tag的內容
async function setTags() {
  const notebookTagList = document.querySelector(".notebookTagsCtn .tagList");
  const noteTagListCtn = document.querySelector(".noteTagsCtn .tagList");
  const path = `/api/notebooks/${notebookId}/tags`;
  const noteTagPath = `/api/notebooks/${notebookId}/notes/${URL_noteId}/tags`;
  const data = await fetchData(path, "GET");
  const notesTag = await fetchData(noteTagPath, "GET");
  for (let i = 0; i < data.tag.length; i++) {
    const noteTag = genNoteTags(data.tag[i], notesTag.tag);
    const notebookTag = genNotebookTags(data.tag[i]);
    notebookTagList.appendChild(notebookTag);
    noteTagListCtn.appendChild(noteTag);
  }
}

//傳入一個物件，key: tag, key: tagId
function genNoteTags(tagName, notesTag) {
  const tagDiv = document.createElement("div");
  const tag = document.createElement("p");
  tagDiv.classList.add("tagItem");
  tag.textContent = tagName.name;
  tag.dataset.tagId = tagName.tagId;
  for (let i = 0; i < notesTag.length; i++) {
    if (tagName.name === notesTag[i].name) {
      console.log(notesTag[i].name);
      tagDiv.classList.toggle("selected");
    }
  }
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
  tag.textContent = tagName.name;
  tag.dataset.tagId = tagName.tagId;
  trash.src = "/static/resource/images/trash-white.png";
  trash.dataset.tag = tagName.name;
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
      const tag = {
        name: "#" + input,
        tagId: result.tagId,
      };
      notebookTagList.appendChild(genNotebookTags(tag));
      console.log(input);
      noteTagList.appendChild(genNoteTags(tag));
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

function updateNoteTagListener() {
  const doneBtn = document.querySelector("#done");
  doneBtn.addEventListener("click", async () => {
    const updateList = [];
    const tagList = document.querySelectorAll(".tagItem");
    tagList.forEach((tag) => {
      if (tag.classList.contains("selected")) {
        updateList.push({
          name: tag.querySelector("p").textContent,
          tagId: tag.querySelector("p").dataset.tagId,
        });
      }
    });
    const path = `/api/notebooks/${notebookId}/notes/${URL_noteId}/tags`;
    const result = await fetchData(path, "PUT", { tags: updateList });
    if (result.result) {
      const noteTagsCtn = document.querySelector(".noteTagsCtn");
      noteTagsCtn.classList.toggle("display-none");
      MsgMaker.success("update tag success!");
    } else {
      MsgMaker.error("update tag faild");
    }
  });
}
notePageMainInit();
