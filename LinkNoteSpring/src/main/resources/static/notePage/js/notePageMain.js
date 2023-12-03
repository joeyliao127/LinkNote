async function notePageMainInit() {
  showTagsBtnListener();
  setTags();
  updateNoteTagListener();
  addTagBtnListener();
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

function addTagBtnListener() {
  const addTagBtn = document.querySelector("#addTag");
  addTagBtn.addEventListener("click", async () => {
    const input = document.querySelector("#createTag").value;
    console.log(input);
    const path = `/api/notebooks/${notebookId}/tags`;
    const result = await fetchData(path, "POST", { tag: input });
    console.log(result);
    if (result.result) {
      const notebookTagList = document.querySelector(
        ".notebookTagsCtn .tagList"
      );
      const noteTagsCtn = document.querySelector(".notebookTagsCtn");
      const noteTagList = noteTagsCtn.querySelector(".tagList");
      noteTagList.appendChild(genNoteTags(input));
      notebookTagList.appendChild(genNotebookTags(input));
      noteTagsCtn.classList.toggle("display-none");
      MsgMaker.success("Create new tag success");
    } else if (result.msg === "重複的資料") {
      MsgMaker.error("tag already exist.");
    } else {
      MsgMaker.error("Create new tag failed");
    }
  });
}

function setNoteTags() {}
notePageMainInit();
