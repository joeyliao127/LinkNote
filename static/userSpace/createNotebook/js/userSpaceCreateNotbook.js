const createNotebookPage = document.querySelectorAll(".createNotebookPage");

function createNotebookInit() {
  cancelCreateForm();
  toPageOne();
  toPageTwo();
}
function cancelCreateForm() {
  const cancelBtn = document.querySelector("#cancel");
  const noteConsoleArea = document.querySelector(".main-area-noteConsole");
  const createNotebookArea = document.querySelector(
    ".main-area-createNotebook"
  );
  cancelBtn.addEventListener("click", () => {
    noteConsoleArea.classList.add("toNote");
    noteConsoleArea.classList.remove("toForm");
    createNotebookArea.classList.add("toNote");
    createNotebookArea.classList.remove("toForm");
  });
}

function toPageTwo() {
  const toPageTwoBtn = document.querySelector("#nextPage");
  toPageTwoBtn.addEventListener("click", () => {
    createNotebookPage.forEach((item) => {
      item.classList.toggle("pageOne");
      item.classList.toggle("pageTwo");
    });
  });
}

function toPageOne() {
  const toPageOneBtn = document.querySelector("#back");
  toPageOneBtn.addEventListener("click", () => {
    createNotebookPage.forEach((item) => {
      item.classList.toggle("pageOne");
      item.classList.toggle("pageTwo");
    });
  });
}
createNotebookInit();
