function notePageMainInit() {
  createNote();
  selectNoteTagsListener();
  updateNoteTagListener();
}

function createNote() {
  const createNoteBtn = document.querySelector("#newNoteBtn");
  createNoteBtn.addEventListener("click", async () => {
    const noteGroup = document.querySelector(".notes-group");
    console.log(noteGroup);
    const path = `/api/notebooks/${notebookId}/notes`;
    const noteId = await fetchData(path, "POST");
    createNoteTitle(noteGroup, false, "new note", true, noteId.noteId);
  });
}

function createNoteTitle(notesGroup, star, title, select, noteId) {
  const noteItem = document.createElement("div");
  const noteTitle = document.createElement("p");
  noteItem.classList.add("note-item");
  noteItem.classList.add("flex");
  noteItem.setAttribute("data-noteId", noteId);
  noteTitle.textContent = title;
  noteItem.appendChild(noteTitle);
  if (select) {
    noteItem.classList.add("selected");
  }
  if (star) {
    const img = document.createElement("img");
    img.setAttribute("src", "/static/resource/images/star-full.png");
    noteItem.appendChild(img);
  }
  notesGroup.appendChild(noteItem);
}

function setNoteContent(name, question, content, keypoint) {
  const noteName = document.querySelector("#noteName");
  const noteQuestion = document.querySelector("#question");
  const noteContent = document.querySelector("#noteContent");
  const noteKeypoint = document.querySelector("#distill");
  noteName.value = name;
  noteQuestion.value = question;
  noteContent.value = content;
  noteKeypoint.value = keypoint;
}

function selectNoteTagsListener() {
  const tagBtn = document.querySelector("#noteTagBtn");
  tagBtn.addEventListener("click", () => {
    const tagList = document.querySelectorAll(".tagList");
    tagList[1].classList.toggle("display-none");
    const tags = tagList[1].querySelectorAll(".tagItem");
    console.log(tags);
    tags.forEach((tag) => {
      console.log(tag);
      tag.addEventListener("click", () => {
        console.log(`被點擊`);
        tag.classList.toggle("selected");
      });
    });
  });
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
      MsgMaker.showMsg(MsgMaker.success, "update tag success!");
    } else {
      MsgMaker.showMsg(MsgMaker.error, "update tag faild");
    }
  });
}

notePageMainInit();
