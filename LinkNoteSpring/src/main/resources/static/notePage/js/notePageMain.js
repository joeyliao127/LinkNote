function notePageMainInit() {
  showNoteTagsBtnListener();
  updateNoteTagListener();
}

function showNoteTagsBtnListener() {
  const tagBtn = document.querySelector("#noteTagBtn");
  tagBtn.addEventListener("click", () => {
    const tagList = document.querySelectorAll(".tagList");
    tagList[1].classList.toggle("display-none");
    const tags = tagList[1].querySelectorAll(".tagItem");
    tags.forEach((tag) => {
      tag.addEventListener("click", () => {
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
