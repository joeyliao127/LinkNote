function sideBarInit() {
  switchNotebooksTpyeBtnListener();
  newNotebookBtnListener();
  signOutBtnListener();
  getUserInfo();
}

function switchNotebooksTpyeBtnListener() {
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

function newNotebookBtnListener() {
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

function signOutBtnListener() {
  const btn = document.querySelector(".sideBar-item-signoutBtn");
  btn.addEventListener("click", () => {
    localStorage.removeItem("token");
    window.location.href = "/userSpace.html";
  });
}

async function getUserInfo() {
  const token = localStorage.getItem("token");
  const response = await fetch(apiUrl + "/api/user", {
    headers: {
      Authorization: "Bearer " + token,
      "Content-Type": "application/json",
    },
    method: "GET",
  });
  const data = await response.json();
  if (data.result) {
    const username = document.querySelector(".sideBar-ctn-welcome p");
    const email = document.querySelector("sideBar-item-userEmail p");
    username.textContent = data.username;
    email.textContent = email;
  }
}
sideBarInit();
