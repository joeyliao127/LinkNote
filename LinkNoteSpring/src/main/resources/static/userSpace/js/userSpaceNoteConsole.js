function userSpaceNoteConsoleInit() {
  noteCardLitener();
  createNoteListener();
  setNotebooks();
}

function noteCardLitener() {
  const noteCardList = document.querySelectorAll(".main-item-noteCard");
  noteCardList.forEach((card) => {
    card.addEventListener("click", () => {
      window.location.href = "/notebooks/1/notes/1";
    });
  });
}

function createNoteListener() {
  const newNoteBtn = document.querySelector(".newNoteBtn");
  newNoteBtn.addEventListener("click", () => {
    // const notebooks
    // fetchData()
  });
}
function setNotebooks() {
  const myNotebook = document.querySelector(".myNotebook");
  console.log(myNotebook);
  const coNotebook = document.querySelector(".coNotebook");
  console.log(coNotebook);
  const notebookPath = `/api/notebooks?offset=0&limit=20&coNotebook=false`;
  const coNotebookPath = `/api/notebooks?offset=0&limit=20&coNotebook=true`;
  genNotebooks(myNotebook, notebookPath);
  genNotebooks(coNotebook, coNotebookPath);

  async function genNotebooks(notebookCtn, path) {
    const GetNotebooksRes = await fetchData(path, "GET");
    console.log(`開始產生notebook`);
    for (let notebook of GetNotebooksRes.notebooks) {
      const notebookTitle = document.createElement("div");
      notebookTitle.classList.add("notebook");
      notebookTitle.textContent = notebook.name;
      notebookTitle.dataset.notebookId = notebook.notebookId;
      notebookTitle.dataset.description = notebook.description;
      notebookCtn.appendChild(notebookTitle);
    }
  }
}

userSpaceNoteConsoleInit();
