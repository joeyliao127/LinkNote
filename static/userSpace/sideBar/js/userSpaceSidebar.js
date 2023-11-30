const token = localStorage.getItem("token");
function sideBarInit() {
  switchNotebooksTpyeBtn();
  newNotbookBtn();
  signOutBtn();
  setUserInfo();
  lazyLoading();
}

function switchNotebooksTpyeBtn() {
  const switchNotebookBtn = document.querySelector(
    ".sideBar-group-notebooksType img"
  );

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

function newNotbookBtn() {
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

function signOutBtn() {
  const btn = document.querySelector(".sideBar-item-signoutBtn");
  btn.addEventListener("click", () => {
    localStorage.removeItem("token");
    window.location.href = "/";
  });
}

async function setUserInfo() {
  const data = await fetchData("/api/user", "GET");
  const username = document.querySelector(".sideBar-ctn-welcome p");
  username.textContent = data.username;
  const email = document.querySelector(".sideBar-item-userEmail p");
  email.textContent = data.email;
}

async function setMyNotebooks() {
  const data = await fetchData("/api/notebooks/");
}

sideBarInit();
