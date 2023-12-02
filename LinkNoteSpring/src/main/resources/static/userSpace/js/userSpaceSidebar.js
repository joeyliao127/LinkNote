function sideBarInit() {
  switchNotebooksTpyeBtn();
  newNotbookBtn();
  signOutBtn();
  setUserInfo();
  setNotebooks();
}

function switchNotebooksTpyeBtn() {
  const switchNotebookBtn = document.querySelector(
    ".sideBar-group-notebooksType img"
  );

  const typeName = document.querySelector(".sideBar-group-notebooksType p");
  const myNotebooksCtn = document.querySelector(".myNotebooks");
  const coNotebookCtn = document.querySelector(".coNotebooks");
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

function setNotebooks() {
  const myNotebook = document.querySelector(".myNotebooks");
  const coNotebook = document.querySelector(".coNotebooks");
  const notebookPath = `/api/notebooks?offset=0&limit=20&coNotebook=false`;
  const coNotebookPath = `/api/notebooks?offset=0&limit=20&coNotebook=true`;
  genNotebooks(myNotebook, notebookPath, false);
  genNotebooks(coNotebook, coNotebookPath, true);

  async function genNotebooks(notebookCtn, path, isCoNotebook) {
    const GetNotebooksRes = await fetchData(path, "GET");

    for (let notebook of GetNotebooksRes.notebooks) {
      const notebookTitle = document.createElement("div");
      notebookTitle.textContent = notebook.name;
      notebookTitle.dataset.name = notebook.name;
      notebookTitle.dataset.notebookId = notebook.notebookId;
      notebookTitle.dataset.description = notebook.description;

      if (isCoNotebook) {
        notebookTitle.classList.add("coNotebook");
      } else {
        notebookTitle.classList.add("notebook");
      }

      notebookCtn.appendChild(notebookTitle);
    }
    notebooksBtnListener(isCoNotebook);
  }
}

function notebooksBtnListener(isCoNotebook) {
  let notebooks;
  if (isCoNotebook) {
    notebooks = document.querySelectorAll(".coNotebook");
  } else {
    notebooks = document.querySelectorAll(".notebook");
  }

  let selectedId = -1;

  for (let i = 0; i < notebooks.length; i++) {
    notebooks[i].addEventListener("click", async () => {
      if (selectedId == -1) {
        notebooks[i].classList.toggle("selected");
      } else {
        notebooks[selectedId].classList.toggle("selected");
        notebooks[i].classList.toggle("selected");
      }
      setNotes(notebooks[i]);
      selectedId = i;
    });
  }
}

let notesDataMap = {};
//傳入notebook物件，取得notebookId & name & description
async function setNotes(notebook) {
  const notebookName = notebook.dataset.name;
  if (!notesDataMap.hasOwnProperty(notebookName)) {
    notesDataMap[notebookName] = {};
    notesDataMap[`${notebookName}`]["name"] = notebookName;
    notesDataMap[`${notebookName}`]["description"] =
      notebook.dataset.description;
    notesDataMap[`${notebookName}`]["notebookId"] = notebook.dataset.notebookId;
    const path = `/api/notebooks/${notebook.dataset.notebookId}/notes?offset=0&limit=20`;
    const result = await fetchData(path, "GET");
    notesDataMap[notebookName]["notes"] = result.notes;
    console.log(notesDataMap);
  }
  const notebookNameCtn = document.querySelector(".main-group-subjectInfo h2");
  const descriptionCtn = document.querySelector(".main-group-subjectInfo p");
  notebookNameCtn.dataset.notebookId = notesDataMap[notebookName].notebookId;
  notebookNameCtn.textContent = notesDataMap[notebookName].name;
  descriptionCtn.textContent = notesDataMap[notebookName].description;
  const noteCardCtn = document.querySelector(".main-group-notes");
  noteCardCtn.innerHTML = "";
  notesDataMap[notebookName].notes.forEach((note) => {
    console.log(`創建子物件`);
    const noteCard = document.createElement("div");
    const star = document.createElement("img");
    const noteName = document.createElement("h3");
    const qeustion = document.createElement("p");
    const createDate = document.createElement("p");
    noteCard.classList.add("main-item-noteCard");
    noteCard.classList.add("flex");
    createDate.classList.add("date");
    if (note.star) {
      star.src = "/static/resource/images/star-full.png";
    } else {
      star.src = "/static/resource/images/star-empty.png";
    }

    star.alt = "star";
    star.classList.add("btn");
    noteName.textContent = note.name;
    qeustion.textContent = note.qeustion;
    createDate.textContent = note.createDate.split(" ")[0];
    noteCard.appendChild(star);
    noteCard.appendChild(noteName);
    noteCard.appendChild(qeustion);
    noteCard.appendChild(createDate);
    noteCardCtn.appendChild(noteCard);
  });
}

//取得notebook -> 觀察notebook -> fetch notebook -> 觀察notebook

sideBarInit();
