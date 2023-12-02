function notePageMainInit() {
  selectNoteTagsListener();
  updateNoteTagListener();
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
      MsgMaker.success("update tag success!");
    } else {
      MsgMaker.error("update tag faild");
    }
  });
}

notePageMainInit();
