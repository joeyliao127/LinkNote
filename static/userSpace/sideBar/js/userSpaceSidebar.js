function sideBarInit() {
  switchNotebooksTpyeBtn();
  newNotbookBtn();
  signOutBtn();
  const token = localStorage.getItem("token");
  console.log(`有token? ${token}`);
  if (token) {
    verifyUserToken(token);
  } else {
    window.location.href = "/";
  }
}
async function verifyUserToken(token) {
  const response = await fetch(apiUrl + "/api/user/auth", {
    headers: {
      Authorization: "Bearer " + token,
      "Content-Type": "application/json",
    },
    method: "POST",
  });
  const verifyResult = await response.json();
  console.log(`token驗證結果：`);
  console.log(verifyResult);
  if (!verifyResult.parseResult) {
    localStorage.removeItem("token");
  }
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

sideBarInit();
