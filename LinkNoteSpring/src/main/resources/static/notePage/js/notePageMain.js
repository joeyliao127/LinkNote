async function notePageMainInit() {
  await setTags();
  displayTagsBtnListener();
  updateNoteTagListener();
  createNewTagBtnListener();
  saveNoteBtnListener();
  clearNoteTagsBtnListener();
  updateStarBtnListener();
  deleteNoteBtnListener();
  // tagListMouseLeftListener();
}

//點選撰寫筆記區的tag按鈕後，顯示notebook所有標籤
function displayTagsBtnListener() {
  const tagBtn = document.querySelectorAll(".tagBtn");
  const nbTagList = document.querySelector(".notebookTagsCtn");
  const ntTagList = document.querySelector(".noteTagsCtn");

  tagBtn[0].addEventListener("click", () => {
    nbTagList.classList.toggle("display-none");
    ntTagList.setAttribute("class", "tagListCtn noteTagsCtn flex display-none");
  });

  tagBtn[1].addEventListener("click", () => {
    // nbTagList.classList.remove("display-none");
    ntTagList.classList.toggle("display-none");
    nbTagList.setAttribute(
      "class",
      "tagListCtn notebookTagsCtn flex display-none"
    );
  });
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

//傳入兩個物件，tagName = key: tag, key: tagId
//notesTag是查詢note有哪些tag
function genNoteTags(tagName, notesTag) {
  const tagItem = document.createElement("div");
  const tag = document.createElement("p");
  tagItem.classList.add("tagItem");
  tagItem.dataset.tagName = tagName.name;
  tagItem.dataset.tagId = tagName.tagId;
  tag.textContent = tagName.name;
  tag.dataset.tagId = tagName.tagId;
  for (let i = 0; i < notesTag.length; i++) {
    if (tagName.name === notesTag[i].name) {
      tagItem.classList.toggle("selected");
    }
  }
  tagItem.appendChild(tag);
  tagItem.addEventListener("click", () => {
    tagItem.classList.toggle("selected");
  });
  return tagItem;
}

function genNotebookTags(tagData) {
  const tagItem = document.createElement("div");
  const tag = document.createElement("p");
  const trash = document.createElement("img");
  const tagName = tagData.name;
  const tagId = tagData.tagId;
  tagItem.classList.add("flex");
  tagItem.classList.add("tagItem");
  tag.textContent = tagName;
  tagItem.dataset.tagId = tagId;
  tagItem.dataset.tagData = tagName;
  trash.src = "/static/resource/images/trash-white.png";
  trash.dataset.tag = tagName;
  tagItem.appendChild(tag);
  tagItem.appendChild(trash);
  tagItem.addEventListener("click", async () => {
    const lastTag = document.querySelector(".tagItem .selected ");
    const currentTag = tagItem.querySelector("p");
    if (lastTag != null) {
      if (lastTag === currentTag) {
        lastTag.classList.remove("selected");
        filter.tag = null;
        filterBtHighLightSwitcher();
        return;
      }
      lastTag.classList.remove("selected");
    }
    currentTag.classList.add("selected");
    filter.noteBox = false;
    filter.tag = tagName;

    filterBtHighLightSwitcher();
  });
  delNotebookTagListener(trash, tagName, tagId);
  tagItem.addEventListener("click", () => {
    const nbTagList = document.querySelector(".notebookTagsCtn");
    nbTagList.classList.toggle("display-none");
  });
  return tagItem;
}

function delNotebookTagListener(trash, tagName, tagId) {
  trash.addEventListener("click", async () => {
    if (!window.confirm("Delete this tag?")) {
      return;
    }
    const path = `/api/notebooks/${notebookId}/tags?tag=${tagName}`;
    const result = await fetchData(path, "DELETE");
    if (result.result) {
      const deletedTagBtns = document.querySelectorAll(
        `.tagItem[data-tag-id='${tagId}']`
      );
      deletedTagBtns.forEach((btn) => {
        btn.remove();
      });
      MsgMaker.success("deleted!");
    } else {
      MsgMaker.error("delete tag failed");
    }
  });
}

function genStarNote() {}
//新增筆記本標籤
function createNewTagBtnListener() {
  const addTagBtn = document.querySelector("#addTag");
  addTagBtn.addEventListener("click", async () => {
    const input = document.querySelector("#createTag").value;
    document.querySelector("#createTag").value = "";

    const path = `/api/notebooks/${notebookId}/tags`;
    const result = await fetchData(path, "POST", { tag: input });

    if (result.result) {
      const notebookTagsCtn = document.querySelector(".notebookTagsCtn");
      const notebookTagList = notebookTagsCtn.querySelector(".tagList");
      const noteTagList = document.querySelector(".noteTagsCtn .tagList");
      const tag = {
        name: input,
        tagId: result.tagId,
      };
      notebookTagList.appendChild(genNotebookTags(tag));

      noteTagList.appendChild(genNoteTags(tag, []));
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
  saveBtn.addEventListener("click", updateNoteContent);
}

async function updateNoteContent() {
  const name = document.querySelector("#noteName").value;
  if (name === "" || name.trim() === "" || name === null) {
    MsgMaker.error("note name is null");
    return;
  }
  const noteId = document.querySelector("#noteName").dataset.noteId;
  const question = document.querySelector("#question").value;
  const content = document.querySelector("#noteContent").value;
  const keypoint = document.querySelector("#keypoint").value;
  const body = {
    name,
    question,
    content,
    keypoint,
  };
  const path = `/api/notebooks/${notebookId}/notes/${noteId}`;
  const result = await fetchData(path, "PUT", body);
  if (result.result) {
    const noteBtns = document.querySelectorAll(".note-item");
    for (note of noteBtns) {
      if (noteId === note.dataset.noteId) {
        note.querySelector("p").textContent = name;
      }
    }
    MsgMaker.success("save note success");
  } else {
    MsgMaker.error("save note failed");
  }
}

function updateStarBtnListener() {
  const starBtn = document.querySelector("#starBtn");
  starBtn.addEventListener("click", async () => {
    let star = starBtn.dataset.star;
    const noteId = document.querySelector("#noteName").dataset.noteId;
    const path = `/api/notebooks/${notebookId}/notes/${noteId}/star`;
    const starImg = starBtn.querySelector("img");

    if (star === "true") {
      star = false;
      starImg.setAttribute("src", "/static/resource/images/star-empty.png");
      starBtn.dataset.star = false;
    } else {
      star = true;
      starImg.setAttribute("src", "/static/resource/images/star-full.png");
      starBtn.dataset.star = true;
    }

    const result = await fetchData(path, "PUT", { star });
    if (result.result) {
      const noteBtns = document.querySelectorAll(".note-item");
      for (note of noteBtns) {
        if (noteId === note.dataset.noteId) {
          if (star) {
            const img = document.createElement("img");
            img.setAttribute("src", "/static/resource/images/star-full.png");
            note.appendChild(img);
          } else {
            const img = note.querySelector("img");
            img.remove();
          }
        }
      }
      MsgMaker.success("Updated!");
    } else {
      MsgMaker.error("update failed");
    }
  });
}

function updateNoteTagListener() {
  const doneBtn = document.querySelector("#done");
  doneBtn.addEventListener("click", async () => {
    const noteTagsCtn = document.querySelector(".noteTagsCtn");
    noteTagsCtn.classList.remove("display-none");
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
      const ntTagList = document.querySelector(".noteTagsCtn");
      ntTagList.classList.toggle("display-none");
      MsgMaker.success("update tag success!");
    } else {
      MsgMaker.error("update tag faild");
    }
  });
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

function deleteNoteBtnListener() {
  const delBtn = document.querySelector("#delete");
  delBtn.addEventListener("click", deleteNote);
}

async function deleteNote() {
  const confirmDel = window.confirm("Delete this note?");
  if (!confirmDel) {
    return;
  }
  localStorage.setItem("noteId", null);
  const path = `/api/notebooks/${notebookId}/notes/${URL_noteId}`;
  const result = await fetchData(path, "DELETE");
  if (result.result) {
    const noteBtn = document.querySelector(
      `.note-item[data-note-id='${URL_noteId}']`
    );
    noteBtn.remove();
    displayLastReadtNote();
  }
}
notePageMainInit();
