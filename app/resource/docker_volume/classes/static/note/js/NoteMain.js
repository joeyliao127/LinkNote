class NoteMain {
  constructor() {
    this.#init();
  }
  async #init() {
    this.#notebookId = this.#getId("notebook");
    this.#noteId = this.#getId("note");
    this.#notes = await this.#getNotes();
    this.#renderNoteCtn();
    this.#renderSideBar(this.#notes);
    await this.#setNoteId(this.#getId("note"));
    this.#renderMain();
  }

  #filter = {
    limit: 20,
    offset: 0,
    star: false,
    sortDesc: false,
    tag: null,
    keyword: null,
  };

  #setFilter(key, value) {
    if (key === null) {
      this.#filter = {
        limit: 20,
        offset: 0,
        star: false,
        sortDesc: false,
        tag: null,
        keyword: null,
      };
    } else {
      this.#filter[key] = value;
    }
    this.#renderNoteCtn();
  }

  #renderTagFilter(tagItem) {
    const tags = document.querySelectorAll(".notebookTagCtn .tag");
    tags.forEach((tagBtn) => {
      if (tagBtn.querySelector("p") !== tagItem.querySelector("p")) {
        tagBtn.classList.remove("selected");
      } else {
        tagItem.classList.add("selected");
      }
    });
    document.querySelector(".notebookTagCtn").classList.add("display-none");
    this.#renderFilterSelect();
  }

  #renderFilterSelect() {
    if (this.#filter.star) {
      document
        .querySelector(".starBtn .filterCheck")
        .classList.remove("display-none");
    } else {
      document
        .querySelector(".starBtn .filterCheck")
        .classList.add("display-none");
    }

    if (this.#filter.sortDesc) {
      document
        .querySelector(".sortBtn .filterCheck")
        .classList.remove("display-none");
    } else {
      document
        .querySelector(".sortBtn .filterCheck")
        .classList.add("display-none");
    }

    if (!this.#filter.star && !this.#filter.sortDesc) {
      document
        .querySelector(".allNoteBtn .filterCheck")
        .classList.remove("display-none");
    } else {
      document
        .querySelector(".allNoteBtn .filterCheck")
        .classList.add("display-none");
    }
  }

  #hiddenFilterCtn() {
    document.querySelector(".filterCtn").classList.add("display-none");
    document.querySelector(".tagCtn").classList.add("display-none");
  }
  #notebookId;
  #noteId;

  async #setNoteId(noteId) {
    const url = `/notebooks/${this.#notebookId}/notes/${noteId}`;
    window.history.pushState(null, null, url);
    this.#noteId = noteId;
    this.#renderNote();
  }

  #notes;
  #notebookTags = [];
  #note;
  #noteContent;
  #noteTags = [];
  #getId(target) {
    const url = window.location.href.split("/");

    if (target === "note") {
      return url[url.length - 1];
    } else {
      return url[url.length - 3];
    }
  }

  async #getNotes() {
    let offset = 0;
    let notes = {
      notes: [],
      tags: [],
      notebookName: null,
    };
    while (true) {
      const path = `/api/notebooks/${this.#notebookId}/notes?limit=${
        this.#filter.limit
      }&offset=${offset}&sortDesc=${this.#filter.sortDesc}&tag=${
        this.#filter.tag
      }&keyword=${this.#filter.keyword}&star=${this.#filter.star}`;

      const response = await FetchDataHandler.fetchData(path, "GET");

      if (!response.ok) {
        MessageMaker.failed("Error: Get notes failed");
        throw new Error("Error: Get notes failed");
      }

      const data = await response.json();

      if (offset === 0) {
        notes.notebookName = data.notebookName;
        notes.tags.push(...data.tags);
      }

      notes.notes.push(...data.notes);

      if (!data.nextPage) {
        return notes;
      }
      offset += 20;
    }
  }

  #renderSideBar() {
    hiddenSideBarBtnListener();
    setNotebookName(this.#notes.notebookName);
    setUserEmail();
    this.#renderNotebookTags();
    this.#createNoteBntListener();
    tagBtnListener();
    displayFilterBtnListener();
    signOutBtnListener();
    document
      .querySelector(".searchNote input")
      .addEventListener("keypress", (e) => {
        if (e.key === "Enter") {
          const keyword = document.querySelector(".searchNote input").value;
          this.#setFilter("keyword", keyword);
        }
      });

    document
      .querySelector(".searchNote input")
      .addEventListener("click", (e) => {
        if (e.key === "Enter") {
          this.#hiddenFilterCtn();
        }
      });

    document.querySelector(".searchNote img").addEventListener("click", () => {
      const keyword = document.querySelector(".searchNote input").value;
      this.#setFilter("keyword", keyword);
    });

    const filterCtn = document.querySelector(".filterCtn");
    const allNoteBtn = document.querySelector(".allNoteBtn");
    const starBtn = document.querySelector(".starBtn");
    const sortBtn = document.querySelector(".sortBtn");

    allNoteBtn.addEventListener("click", () => {
      this.#setFilter(null);
      filterCtn.classList.add("display-none");
      this.#renderFilterSelect();
    });

    starBtn.addEventListener("click", () => {
      const hasStar = starBtn
        .querySelector(".filterCheck")
        .classList.contains("display-none");

      if (hasStar) {
        this.#setFilter("star", true);
      } else {
        this.#setFilter("star", false);
      }

      filterCtn.classList.add("display-none");
      this.#renderFilterSelect();
    });

    sortBtn.addEventListener("click", () => {
      const hasSort = sortBtn
        .querySelector(".filterCheck")
        .classList.contains("display-none");

      if (hasSort) {
        this.#setFilter("sortDesc", true);
      } else {
        this.#setFilter("sortDesc", false);
      }

      filterCtn.classList.add("display-none");
      this.#renderFilterSelect();
    });

    function hiddenSideBarBtnListener() {
      document
        .querySelector("#hiddenSideBarBtn")
        .addEventListener("click", () => {
          document.querySelector(".sideBar").classList.remove("displaySideBar");
          document.querySelector(".filterCtn").classList.add("display-none");
          document
            .querySelector(".notebookTagCtn")
            .classList.add("display-none");
        });
    }

    function setNotebookName(notebookName) {
      document.querySelector(".notebookName").textContent = notebookName;
    }

    function displayFilterBtnListener() {
      document.querySelector(".filter").addEventListener("click", () => {
        document.querySelector(".filterCtn").classList.toggle("display-none");
        document.querySelector(".notebookTagCtn").classList.add("display-none");
      });
    }

    function tagBtnListener() {
      document
        .querySelector(".notebookTagBtn")
        .addEventListener("click", () => {
          document
            .querySelector(".notebookTagCtn")
            .classList.toggle("display-none");
          document.querySelector(".filterCtn").classList.add("display-none");
        });
    }
    function setUserEmail() {
      document.querySelector(".userEmail p").textContent =
        localStorage.getItem("email");
    }

    function signOutBtnListener() {
      document.querySelector(".signoutBtn").addEventListener("click", () => {
        localStorage.clear();
        window.location.href = "/";
      });
    }
  }

  #createNoteBntListener() {
    document
      .querySelector(".createNoteBtn")
      .addEventListener("click", async () => {
        try {
          const notebookId = this.#notebookId;
          const path = `/api/notebooks/${notebookId}/notes`;
          const response = await FetchDataHandler.fetchData(path, "POST", {
            notebookId,
          });
          if (!response.ok) {
            throw new Error("Error: create note error.");
          }
          const data = await response.json();
          const noteId = data.noteId;
          const note = {
            noteId,
            name: "new note",
          };
          this.#setNoteId(noteId);
          window.location.href = `/notebooks/${
            this.#notebookId
          }/notes/${noteId}`;
        } catch (e) {
          console.log(e);
          MessageMaker.failed("Error: create note error.");
        }
      });
  }

  #renderNotebookTags() {
    const tagArea = document.querySelector(".tagArea");

    this.#notes.tags.forEach((tag) => {
      const tagItem = genTag(tag);
      this.#notebookTags.push(tag);
      tagItem.addEventListener("click", () => {
        tagItem.classList.add("selected");
        this.#setFilter("tag", tag.name);
        this.#renderTagFilter(tagItem);
      });

      tagItem
        .querySelector(".deleteTagBtn")
        .addEventListener("click", async () => {
          const path = `/api/notebooks/${this.#notebookId}/tags?tagId=${
            tag.tagId
          }`;
          const response = await FetchDataHandler.fetchData(path, "DELETE");
          if (response.ok) {
            MessageMaker.success(`Remove tag ${tag.name} success!`);

            this.#renderTagFilter();
            this.#setFilter(null);
            tagItem.remove();
          }
        });
      tagArea.appendChild(tagItem);
    });

    createTagBtnListener(this.#notebookId);

    function genTag(tag) {
      const tagItem = document.createElement("div");
      tagItem.classList.add("tag");
      tagItem.innerHTML = `
        <p>${tag.name}</p>
        <div class="deleteTagBtn">
          <p>Removev</p>
        </div>
      `;

      return tagItem;
    }

    function createTagBtnListener(notebookId) {
      const input = document.querySelector(".createTagForm input");

      document
        .querySelector(".createTagForm input")
        .addEventListener("keypress", (e) => {
          if (e.key === "Enter") {
            const tagName = document.querySelector(
              ".createTagForm input"
            ).value;
            createNotebookTag(tagName, notebookId);
            input.value = "";
          }
        });

      document.querySelector("#createTagBtn").addEventListener("click", () => {
        const tagName = document.querySelector(".createTagForm input").value;
        createNotebookTag(tagName, notebookId);
        input.value = "";
      });

      async function createNotebookTag(tagName, notebookId) {
        const path = `/api/notebooks/${notebookId}/tags`;
        const response = await FetchDataHandler.fetchData(path, "POST", {
          name: tagName,
        });
        if (response.ok) {
          MessageMaker.success("Create tag Success!");
        } else if (response.status === 400) {
          MessageMaker.warning("Duplicate tag.");
          return;
        } else {
          MessageMaker.failed("Create tag Failed!");
          return;
        }

        const tagId = await response.json();
        const tag = {
          name: tagName,
          tagId: tagId.tagId,
        };
        const tagItem = tagArea.appendChild(genTag(tag));
        tagItem
          .querySelector(".deleteTagBtn")
          .addEventListener("click", async () => {
            const path = `/api/notebooks/${notebookId}/tags?tagId=${tag.tagId}`;
            const response = await FetchDataHandler.fetchData(path, "DELETE");
            if (response.ok) {
              MessageMaker.success(`Remove tag ${tag.name} success!`);
              tagItem.remove();
            }
          });
      }
    }
  }

  async #renderNoteCtn() {
    const data = await this.#getNotes();
    const notes = data.notes;
    const noteArea = document.querySelector(".noteArea");
    noteArea.innerHTML = "";
    notes.forEach((note) => {
      noteArea.appendChild(this.#genNote(note, this.#notebookId));
    });
  }

  #genNote(note, notebookId) {
    const noteItem = document.createElement("a");
    noteItem.href = `/notebooks/${this.#notebookId}/notes/${note.noteId}`;
    noteItem.classList.add("note");
    if (note.noteId === this.#noteId) {
      noteItem.classList.add("selected");
    }
    if (note.id === this.#noteId) {
      noteItem.classList.add("selected");
    }
    noteItem.innerHTML = `
      <p>${note.name}</p>
      <div class="star"></div>
    `;
    if (note.star) {
      noteItem.querySelector(".star").classList.add("star-full");
    } else {
      noteItem.querySelector(".star").classList.add("star-empty");
    }
    updateStarBtnListener(note.noteId, note.name, notebookId);
    return noteItem;

    function updateStarBtnListener(noteId, noteName, notebookId) {
      const starBtn = noteItem.querySelector(".star");
      let requestBody;
      starBtn.addEventListener("click", async (e) => {
        e.preventDefault();
        e.stopPropagation();
        const path = `/api/notebooks/${notebookId}/notes/${noteId}`;

        if (starBtn.classList.contains("star-full")) {
          noteItem.querySelector(".star").classList.remove("star-full");
          noteItem.querySelector(".star").classList.add("star-empty");
          requestBody = {
            name: noteName,
            star: false,
          };
        } else {
          noteItem.querySelector(".star").classList.add("star-full");
          noteItem.querySelector(".star").classList.remove("star-empty");
          requestBody = {
            name: noteName,
            star: true,
          };
        }

        const response = await FetchDataHandler.fetchData(
          path,
          "PUT",
          requestBody
        );
        if (!response.ok) {
          MessageMaker.failed("Error: update star Failed.");
          return;
        }
      });
    }
  }

  async #renderNote() {
    this.#renderCollaborators();
    await this.#getNote();
    this.#renderNoteTags();
    this.#deleteNoteBtnListener();
    hiddenSideBarBtnListener();
    noteTagBtnListener();
    collaboratorBtnListener();
    editNoteName();
    setNoteName(this.#note.name);
    function hiddenSideBarBtnListener() {
      document.querySelector("#hamburger").addEventListener("click", () => {
        document.querySelector(".sideBar").classList.add("displaySideBar");
      });
    }

    function setNoteName(name) {
      document.querySelector(".noteName").textContent = name;
    }

    function noteTagBtnListener() {
      document.querySelector(".noteTagBtn").addEventListener("click", () => {
        document.querySelector(".tagForm").classList.toggle("display-none");
      });
    }

    function collaboratorBtnListener() {
      document
        .querySelector(".collaboratorBtn")
        .addEventListener("click", () => {
          document
            .querySelector(".collaboratorCtn")
            .classList.toggle("display-none");
        });
    }

    function editNoteName() {
      const noteName = document.querySelector(".noteName");
      noteName.addEventListener("click", () => {
        noteName.classList.add("editNoteName");
      });
      noteName.addEventListener("blur", () => {
        noteName.classList.remove("editNoteName");
      });
    }
  }

  #renderNoteTags() {
    const noteTagForm = document.querySelector(".tagForm");
    if (this.#notebookTags) {
      const noTag = document.querySelector(".noTag");
      if (noTag) {
        noTag.remove();
      }
    }
    this.#notebookTags.forEach((tag) => {
      const tagItem = document.createElement("div");
      tagItem.classList.add("tag");
      tagItem.innerHTML = `
      <img
        class="checked"
        src="/icons/check.png"
        alt="check"
      />
      <p>${tag.name}</p>
      `;
      if (!this.#noteTags.includes(tag.name)) {
        tagItem.querySelector(".checked").classList.add("display-none");
      }

      tagItem.addEventListener("click", async () => {
        const isCreate = tagItem
          .querySelector(".checked")
          .classList.contains("display-none");

        let response;

        if (isCreate) {
          const path = `/api/notebooks/${this.#notebookId}/notes/${
            this.#noteId
          }/tags`;
          const requestBody = {
            tagId: tag.tagId,
          };
          response = await FetchDataHandler.fetchData(
            path,
            "POST",
            requestBody
          );
        } else {
          const path = `/api/notebooks/${this.#notebookId}/notes/${
            this.#noteId
          }/tags?tagId=${tag.tagId}`;
          response = await FetchDataHandler.fetchData(path, "DELETE");
          if (!response.ok) {
            MessageMaker.failed("Update note tag failed");
            return;
          }
        }

        tagItem.querySelector(".checked").classList.toggle("display-none");
      });

      noteTagForm.appendChild(tagItem);
    });
  }

  async #getNote() {
    try {
      const path = `/api/notebooks/${this.#notebookId}/notes/${this.#noteId}`;
      const response = await FetchDataHandler.fetchData(path, "GET");
      if (!response.ok) {
        throw new Error("Get note failed.");
      }
      const data = await response.json();

      this.#note = data.note;
      this.#noteContent = data.note.content;
      data.tags.forEach((tag) => {
        this.#noteTags.push(tag.name);
      });
    } catch (e) {
      console.log(e);
      MessageMaker.failed("Get note failed.");
    }
  }

  async #renderCollaborators() {
    try {
      const path = `/api/notebooks/${this.#notebookId}/collaborators`;
      const response = await FetchDataHandler.fetchData(path, "GET");
      const data = await response.json();
      document.querySelector(".owner .user").textContent = data.owner.ownerName;
      const collaboratorsCtn = document.querySelector(".collaborators");
      data.collaborators.forEach((collaborator) => {
        const name = document.createElement("p");
        name.textContent = collaborator.name;
        name.classList.add("user");
        collaboratorsCtn.appendChild(name);
      });
    } catch (e) {
      console.log(e);
      MessageMaker.failed("Error: collaborator error.");
    }
  }

  #deleteNoteBtnListener() {
    document.querySelector(".deleteNoteBtn").addEventListener("click", () => {
      const name = document.querySelector(".noteName").textContent;
      const path = `/api/notebooks/${this.#notebookId}/notes/${this.#noteId}`;
      DeleteAlert.renderDeleteAletBox("Note", name, path);
    });
  }

  async #renderMain() {
    const tuiEditor = document.createElement("div");
    tuiEditor.id = "editor";
    document.querySelector("main").appendChild(tuiEditor);
    await this.#getNote();
    let initContent = this.#noteContent;
    if (!initContent) {
      initContent = `# Title\n\n## Question\n\n## Keypoint
      `;
    }

    const { Editor } = toastui;

    const { codeSyntaxHighlight } = Editor.plugin;
    const editor = new Editor({
      el: document.querySelector("#editor"),
      previewStyle: "vertical",
      height: "93vh",
      initialValue: initContent,
      plugins: [codeSyntaxHighlight],
    });

    setInterval(async () => {
      const content = editor.getMarkdown();
      const name = document.querySelector(".noteName").textContent;
      const h2s = document.querySelectorAll("h2");
      let requestBody = {
        name,
        content,
      };
      let question;
      let keypoint;
      h2s.forEach((h2) => {
        if (h2.textContent.toLowerCase() === "question") {
          if (h2.nextElementSibling && h2.nextElementSibling.tagName === "P") {
            question = h2.nextElementSibling.textContent;
            requestBody["question"] = question;
          }
        } else if (
          h2.nextElementSibling &&
          h2.textContent.toLocaleLowerCase() === "keypoint"
        ) {
          if (h2.nextElementSibling.tagName === "P") {
            keypoint = h2.nextElementSibling.textContent;
            requestBody["keypoint"] = keypoint;
          }
        }
      });

      const path = `/api/notebooks/${this.#notebookId}/notes/${this.#noteId}`;
      const response = await FetchDataHandler.fetchData(
        path,
        "PUT",
        requestBody
      );
      if (!response.ok) {
        MessageMaker.failed("失敗.");
      }
    }, 5000);
  }
}

const noteMain = new NoteMain();
