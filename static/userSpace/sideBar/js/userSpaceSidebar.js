function sideBarInit() {
  switchNotebooksTpye();
  newNotbook();
}

function switchNotebooksTpye() {
  const switchNotebookBtn = document.querySelector(
    ".sideBar-group-notebooksType img"
  );
  console.log(switchNotebookBtn);
  const typeName = document.querySelector(".sideBar-group-notebooksType p");
  const myNotebooksCtn = document.querySelector(".myNotebook");
  const coNotebookCtn = document.querySelector(".coNotebook");
  let flag = true;
  switchNotebookBtn.addEventListener("click", () => {
    if (flag) {
      typeName.textContent = "CoNotebooks";
      flag = false;
    } else {
      typeName.textContent = "MyNoteBooks";
      flag = true;
    }
    myNotebooksCtn.classList.toggle("toCoNotebooks");
    coNotebookCtn.classList.toggle("toCoNotebooks");
  });
}

function newNotbook() {
  const newNoteBookBtn = document.querySelector(".sideBar-newNotebookBtn");
  const noteConsoleArea = document.querySelector(".main-area-noteConsole");
  const createNotebookArea = document.querySelector(
    ".main-area-createNotebook"
  );
  newNoteBookBtn.addEventListener("click", () => {
    noteConsoleArea.classList.add("toForm");
    createNotebookArea.classList.add("toForm");
    noteConsoleArea.classList.remove("toNote");
    createNotebookArea.classList.remove("toNote");
  });
}

sideBarInit();
