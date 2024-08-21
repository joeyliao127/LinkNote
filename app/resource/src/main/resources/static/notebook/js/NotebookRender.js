class NotebookRender {
  constructor() {}
  #role;
  #filter = {
    offset: 0,
    limit: 20,
    star: false,
    keyword: null,
    tag: null,
    sortDesc: false,
  };

  resetTagFilter() {
    this.#currentTag = "";
    this.#tagsBtns = [];
  }
  #currentTag;
  #tagsBtns = [];

  #setFilter(key, value) {
    this.#filter[key] = value;
    this.#filter.offset = 0;
    this.#renderToolBarSelected();
  }

  #renderSelectedTag(target) {
    if (target) {
      this.#currentTag = target;
      target.classList.add("selected");
    }

    for (let i = 0; i < this.#tagsBtns.length; i++) {
      if (this.#tagsBtns[i] !== target) {
        console.log(this.#tagsBtns[i]);
        this.#tagsBtns[i].classList.remove("selected");
      }
    }
  }
  #notebooksOffset = 0;
  #notebooksLimit = 20;
  #renderToolBarSelected() {
    if (this.#filter.star) {
      document.querySelector("#starBtn").classList.add("selected");
    } else {
      document.querySelector("#starBtn").classList.remove("selected");
    }
    if (this.#filter.sortDesc) {
      document.querySelector("#sortByTimeBtn").classList.add("selected");
    } else {
      document.querySelector("#sortByTimeBtn").classList.remove("selected");
    }
    if (this.#filter.tag) {
      document.querySelector("#tagBtn").classList.add("selected");
    } else {
      document.querySelector("#tagBtn").classList.remove("selected");
    }
    if (!this.#filter.star && !this.#filter.sortDesc && !this.#filter.tag) {
      document.querySelector("#allNoteBtn").classList.add("selected");
    } else {
      document.querySelector("#allNoteBtn").classList.remove("selected");
    }
  }

  resetFilter() {
    this.#filter = {
      offset: 0,
      limit: 20,
      star: false,
      keyword: null,
      tag: null,
      sort: false,
    };
    this.#currentTag = "";
    this.#renderSelectedTag();
    this.#notebooksOffset = 0;
    this.#notebooksLimit = 20;
  }

  async renderMyNotebooks() {
    this.#role = "owner";
    await this.renderNotebookArea("myNotebooks");
  }

  async renderCoNotebooks() {
    this.#role = "collaborator";
    await this.renderNotebookArea("coNotebooks");
  }

  async renderNotebook(notebook) {
    const notebookArea = document.createElement("section");
    notebookArea.classList.add("notebookArea");
    notebookArea.classList.add("scroll");
    const noteCtn = await this.genNoteCtn(notebook, "myNotebook");
    notebookArea.appendChild(noteCtn);
    ReRenderElement.reRenderMain(notebookArea);
  }

  async renderCoNotebook(notebook) {
    const notebookArea = document.createElement("section");
    notebookArea.classList.add("notebookArea");
    notebookArea.classList.add("scroll");
    const noteCtn = await this.genNoteCtn(notebook, "coNotebook");
    notebookArea.appendChild(noteCtn);
    ReRenderElement.reRenderMain(notebookArea);
  }

  async renderNotebookArea(renderPage) {
    let notebookArea = document.querySelector(".notebookArea");
    if (notebookArea) {
      notebookArea.remove();
    }
    notebookArea = await this.genNotebookArea(renderPage);
    ReRenderElement.reRenderMain(notebookArea);
  }

  async genNotebookArea(renderPage) {
    const notebookArea = document.createElement("section");
    notebookArea.classList.add("notebookArea");
    notebookArea.classList.add("scroll");
    notebookArea.innerHTML = `
    <div class="notebooksTop">
      <h3></h3>
      <div class="search">
        <input
          type="text"
          id="searchNotebook"
          placeholder="search notebook"
        />
        <img
          src="/icons/search.png"
          alt="search"
        />
      </div>
    </div>
    `;

    let path = "";
    const searchElemet = notebookArea.querySelector("#searchNotebook");
    if (this.#role === "owner") {
      path = `/api/notebooks?offset=${this.#filter.offset}&limit=${
        this.#filter.limit
      }`;
      notebookArea.querySelector("h3").textContent = "My Notebook";

      searchElemet.addEventListener("keypress", (e) => {
        const keyword = searchElemet.value;
        if (e.key === "Enter") {
          this.renderNotebookCtn(path + `&keyword=${keyword}`);
          return;
        }
      });
    } else {
      path = `/api/coNotebooks?offset=${this.#filter.offset}&limit=${
        this.#filter.limit
      }`;
      searchElemet.addEventListener("keypress", (e) => {
        const keyword = searchElemet.value;
        if (e.key === "Enter" && keyword) {
          this.renderNotebookCtn(path + `&keyword=${keyword}`);
          return;
        }
      });
      notebookArea.querySelector("h3").textContent = "Collaborative Notebook";
    }
    notebookArea.appendChild(await this.genNotebookCtn(path, renderPage));
    return notebookArea;
  }

  async renderNotebookCtn(path, renderPage) {
    const notebookCtn = document.querySelector(".notebookCtn");
    if (notebookCtn) {
      notebookCtn.remove();
    }
    const newNotebookCtn = await this.genNotebookCtn(path, renderPage);
    newNotebookCtn.classList.add("notebookCtn");
    document.querySelector(".notebookArea").appendChild(newNotebookCtn);
  }

  async genNotebookCtn(path, renderPage) {
    const notebookCtn = document.createElement("div");
    notebookCtn.classList.add("notebookCtn");
    const response = await FetchDataHandler.fetchData(path, "GET");
    let notebooks = await response.json();
    notebooks = notebooks.notebooks;
    if (notebooks.length === 0) {
      if (this.#role === "owner") {
        return this.genNoCoNotebookBlock();
      } else {
        return this.genNoCoNotebookBlock();
      }
    }

    notebooks.forEach(async (notebook) => {
      const divide = document.createElement("div");
      divide.classList.add("divide");
      const noteCtn = await this.genNoteCtn(notebook, renderPage);
      notebookCtn.appendChild(divide);
      notebookCtn.appendChild(noteCtn);
    });

    return notebookCtn;
  }

  renderNoteCtn(notebook) {
    const noteCtn = document.querySelector(".noteCtn");
    if (noteCtn) {
      noteCtn.remove();
    }
    let newNoteCtn = document.createElement("section");
    newNoteCtn.classList.add("noteCtn");
    newNoteCtn.appendChild(this.genNoteCtn(notebook));
    document.querySelector(".notebookCtn").appendChild(newNoteCtn);
  }

  async genNoteCtn(notebookInfo, renderPage) {
    const noteCtn = document.createElement("section");
    noteCtn.classList.add("noteCtn");
    const divideLine = document.createElement("div");
    divideLine.classList.add("divdeLine");
    let notebookBtnCtn;

    if (renderPage === "myNotebooks") {
      notebookBtnCtn = this.#genMyNotebooksToolBar(notebookInfo);
      noteCtn.appendChild(notebookBtnCtn);
    } else if (renderPage === "myNotebook") {
      notebookBtnCtn = await this.#genNotebookToolBar(notebookInfo);

      noteCtn.appendChild(notebookBtnCtn);
    } else if (renderPage === "coNotebooks") {
    } else if (renderPage === "coNotebook") {
      notebookBtnCtn = await this.#genCoNotebookToolBar(notebookInfo.id);
      noteCtn.appendChild(notebookBtnCtn);
    }
    const noteTitle = document.createElement("h5");
    noteTitle.textContent = "Notes";

    noteCtn.appendChild(genNotebookNameElement(notebookInfo.name));

    noteCtn.appendChild(
      genNotebookDescriptionElement(notebookInfo.description, this.#role)
    );

    noteCtn.appendChild(genUpdateBtnCtn(notebookInfo));
    noteCtn.appendChild(noteTitle);


    noteCtn.appendChild(await this.genNoteCardCtn(notebookInfo.id, renderPage));
    return noteCtn;

    function genNotebookDescriptionElement(description, role) {
      const descriptionElement = document.createElement("p");
      descriptionElement.classList.add("description");
      descriptionElement.textContent = description;
      if (role === "owner") {
        descriptionElement.addEventListener("click", () => {
          editMode(descriptionElement);
        });
        descriptionElement.setAttribute("contenteditable", true);
      }
      return descriptionElement;
    }

    function genNotebookNameElement(name) {
      const nameElement = document.createElement("h4");
      nameElement.textContent = name;
      return nameElement;
    }

    function genUpdateBtnCtn(notebookInfo) {
      const updateBtnCtn = document.createElement("div");
      updateBtnCtn.classList.add("updateBtnCtn");
      updateBtnCtn.appendChild(genUpdateBtn(notebookInfo));
      updateBtnCtn.appendChild(
        genCancelUpdateBtn(notebookInfo.description, notebookInfo.name)
      );
      return updateBtnCtn;
    }

    function genUpdateBtn(notebookInfo) {
      const updateBtn = document.createElement("div");
      updateBtn.classList.add("updateBtn");
      updateBtn.classList.add("display-none");
      updateBtn.addEventListener("click", () => {
        updateNotebook(notebookInfo);
      });
      updateBtn.textContent = "Save";
      return updateBtn;
    }

    async function updateNotebook(notebookInfo) {
      const path = `/api/notebooks/${notebookInfo.id}`;
      const name = noteCtn.querySelector("h4").textContent;
      const description = noteCtn.querySelector(".description").textContent;
      const requestBody = {
        name,
        description,
      };

      const response = await FetchDataHandler.fetchData(
        path,
        "PUT",
        requestBody
      );

      if (response.ok) {
        MessageMaker.success("update success!");
        notebookInfo.description = description;
        notebookInfo.name = name;
        cancelEditMode();
      }
    }

    function genCancelUpdateBtn(oldDescription, oldName) {
      const cancelUpdateBtn = document.createElement("div");
      cancelUpdateBtn.classList.add("display-none");
      cancelUpdateBtn.classList.add("cancelUpdateBtn");
      cancelUpdateBtn.textContent = "Cancel";
      cancelUpdateBtn.addEventListener("click", () => {
        cancelEditMode(oldDescription, oldName);
      });
      return cancelUpdateBtn;
    }

    function editMode() {
      noteCtn.querySelector(".description").classList.add("editMode");
      noteCtn.querySelector(".updateBtn").classList.remove("display-none");
      noteCtn
        .querySelector(".cancelUpdateBtn")
        .classList.remove("display-none");
    }

    function cancelEditMode() {
      noteCtn.querySelector(".description").classList.remove("editMode");
      noteCtn.querySelector(".updateBtn").classList.add("display-none");
      noteCtn.querySelector(".cancelUpdateBtn").classList.add("display-none");
    }
  }

  #genMyNotebooksToolBar(notebookInfo) {
    const toolBarCtn = document.createElement("div");
    toolBarCtn.classList.add("toolBar");
    toolBarCtn.appendChild(this.#genDeleteNotebookBtn(notebookInfo));
    return toolBarCtn;
  }

  async #genNotebookToolBar(notebookInfo) {
    const toolBarCtn = document.createElement("div");
    toolBarCtn.classList.add("toolBar");
    toolBarCtn.appendChild(this.#genCreateNoteBtn(notebookInfo.id));
    toolBarCtn.appendChild(this.#genAddCollabortorBtn(notebookInfo.id));
    toolBarCtn.appendChild(this.#genAllNoteBtn(notebookInfo.id, "myNotebook"));
    toolBarCtn.appendChild(
      await this.#genTagBtn(notebookInfo.id, "myNotebook")
    );
    toolBarCtn.appendChild(this.#genSortByTime(notebookInfo.id, "myNotebook"));
    toolBarCtn.appendChild(this.#genStar(notebookInfo.id, "myNotebook"));
    toolBarCtn.appendChild(
      this.#genSearchNoteInput(notebookInfo.id, "myNotebook")
    );
    toolBarCtn.appendChild(this.#genDeleteNotebookBtn(notebookInfo));
    toolBarCtn.appendChild(await this.#genNotebookTagForm(notebookInfo.id));
    toolBarCtn.appendChild(await this.#genCollaboratorForm(notebookInfo.id));
    return toolBarCtn;
  }
  async #genCoNotebookToolBar(notebookId) {
    const toolBarCtn = document.createElement("div");
    toolBarCtn.classList.add("toolBar");
    toolBarCtn.appendChild(this.#genCreateNoteBtn(notebookId));
    toolBarCtn.appendChild(this.#genAllNoteBtn(notebookId, "coNotebook"));
    toolBarCtn.appendChild(await this.#genTagBtn(notebookId, "coNotebook"));
    toolBarCtn.appendChild(this.#genSortByTime(notebookId, "coNotebook"));
    toolBarCtn.appendChild(this.#genStar(notebookId, "coNotebook"));
    toolBarCtn.appendChild(this.#genSearchNoteInput(notebookId, "coNotebook"));
    toolBarCtn.appendChild(await this.#genNotebookTagForm(notebookId));
    toolBarCtn.appendChild(await this.#genCollaboratorForm(notebookId));
    return toolBarCtn;
  }

  #genCreateNoteBtn(notebookId) {
    const createNoteBtn = document.createElement("div");
    createNoteBtn.classList.add("createNoteBtn");
    createNoteBtn.innerHTML = "<p>New note</p>";
    createNoteBtn.addEventListener("click", () => {
      createNote(notebookId);
    });
    return createNoteBtn;

    async function createNote(notebookId) {
      const path = `/api/notebooks/${notebookId}/notes`;
      const response = await FetchDataHandler.fetchData(path, "POST", {
        notebookId,
      });
      if (response.ok) {
        const data = await response.json();
        window.location.href = `/notebooks/${notebookId}/notes/${data.noteId}`;
      }
    }
  }

  #genAddCollabortorBtn() {
    const collaboratorBtn = document.createElement("div");
    collaboratorBtn.classList.add("toolBtn");
    collaboratorBtn.id = "collaboratorBtn";
    collaboratorBtn.innerHTML = ` 
    <img
    src="/icons/addCollaborator.png"
    alt="collaboratorBtn"
  />`;
    collaboratorBtn.addEventListener("click", () => {
      document
        .querySelector(".collaboratorForm")
        .classList.toggle("display-none");
      document.querySelector(".tagForm").classList.add("display-none");
    });
    return collaboratorBtn;
  }

  #genAllNoteBtn(notebookId, renderPage) {
    const allNoteBtn = document.createElement("div");
    allNoteBtn.classList.add("toolBtn");
    allNoteBtn.id = "allNoteBtn";
    allNoteBtn.innerHTML = `
    <img
        src="/icons/box.png"
        alt="box"
      />`;
    allNoteBtn.addEventListener("click", () => {
      this.resetFilter();
      this.#renderToolBarSelected();
      this.renderNoteCardCtn(notebookId, renderPage);
    });
    return allNoteBtn;
  }

  async #genTagBtn(notebookId, renderPage) {
    const tagBtn = document.createElement("div");
    tagBtn.classList.add("toolBtn");
    tagBtn.id = "tagBtn";
    tagBtn.innerHTML = `
    <img
        src="/icons/tag.png"
        alt="tag"
      />`;

    tagBtn.addEventListener("click", () => {
      document.querySelector(".tagForm").classList.toggle("display-none");
      document.querySelector(".collaboratorForm").classList.add("display-none");
    });
    return tagBtn;
  }

  #genSortByTime(notebookId, renderPage) {
    const sortByTimeBtn = document.createElement("div");
    sortByTimeBtn.classList.add("toolBtn");
    sortByTimeBtn.id = "sortByTimeBtn";
    sortByTimeBtn.innerHTML = `
    <img
        src="/icons/clock.png"
        alt="clock"
      />
    `;
    sortByTimeBtn.addEventListener("click", () => {
      if (this.#filter.sortDesc) {
        this.#setFilter("sortDesc", false);
      } else {
        this.#setFilter("sortDesc", true);
      }
      this.renderNoteCardCtn(notebookId, renderPage);
    });
    return sortByTimeBtn;
  }

  #genStar(notebookId, renderPage) {
    const starBtn = document.createElement("div");
    starBtn.classList.add("toolBtn");
    starBtn.id = "starBtn";
    starBtn.innerHTML = `
    <img
    src="/icons/full-star.png"
    alt="star"
  />
    `;
    starBtn.addEventListener("click", () => {
      if (this.#filter.star) {
        this.#setFilter("star", false);
      } else {
        this.#setFilter("star", true);
      }
      this.renderNoteCardCtn(notebookId, renderPage);
    });
    return starBtn;
  }

  #genSearchNoteInput(notebookId, renderPage) {
    const searchInput = document.createElement("div");
    searchInput.innerHTML = `
    <input
      type="text"
      id="searchNote"
      placeholder="search notes"
    />
    <img
      src="/icons/search.png"
      alt="search"
    />`;
    searchInput.classList.add("searchNote");
    searchInput.classList.add("search");
    searchInput.querySelector("input").addEventListener("keypress", (e) => {
      const keyword = searchInput.querySelector("input");
      if (e.key === "Enter") {
        this.#setFilter("keyword", keyword.value);
        this.renderNoteCardCtn(notebookId, renderPage);
      }
    });
    searchInput.querySelector("img").addEventListener("click", () => {
      const keyword = searchInput.querySelector("input");
      this.#setFilter("keyword", keyword.value);
      this.renderNoteCardCtn(notebookId, renderPage);
    });
    return searchInput;
  }

  #genDeleteNotebookBtn(notebookInfo) {
    const deleteNotebookBtn = document.createElement("div");
    deleteNotebookBtn.classList.add("toolBtn");
    deleteNotebookBtn.innerHTML = `
    <img
      src="/icons/trash-white.png"
      alt="deleteBtn"
    />`;
    deleteNotebookBtn.addEventListener("click", deleteNotebook);

    return deleteNotebookBtn;
    async function deleteNotebook() {
      const path = `/api/notebooks/${notebookInfo.id}`;
      DeleteAlert.renderDeleteAletBox("notebook", notebookInfo.name, path);
    }
  }

  async #genNotebookTagForm(notebookId) {
    const tagForm = document.createElement("div");
    tagForm.classList.add("tagForm");
    tagForm.classList.add("display-none");
    tagForm.innerHTML = `
    <h5>Tags</h5>
    <div class="tagCtn">
    </div>
    <div class="createTagForm">
    <input
      type="text"
      id="createTagName"
      placeholder="Create new tag"
    />
    <button id="createTagBtn">Create</button>
  </div>
    `;
    const path = `/api/notebooks/${notebookId}/tags`;
    const response = await FetchDataHandler.fetchData(path, "GET");
    if (!response.ok) {
      MessageMaker.failed("Get tag Failed.");
      return;
    }
    const data = await response.json();
    const tags = data.tags;
    if (tags.length === 0) {
      return tagForm;
    }
    tags.forEach((tag) => {
      const tagBtn = genTagBtn(tag);
      tagForm.querySelector(".tagCtn").appendChild(tagBtn);
      this.#tagsBtns.push(tagBtn);
      tagBtn.addEventListener("click", () => {
        this.#setFilter("tag", tag.name);
        this.renderNoteCardCtn(notebookId, "myNotebook");
        this.#renderSelectedTag(tagBtn);
        tagForm.classList.add("display-none");
      });
    });

    tagForm
      .querySelector("#createTagBtn")
      .addEventListener("click", async () => {
        createTagHandler();
      });
    tagForm
      .querySelector("#createTagName")
      .addEventListener("keypress", async (e) => {
        if (e.key === "Enter") {
          createTagHandler();
        }
      });

    return tagForm;

    async function createTagHandler() {
      const newTagInput = tagForm.querySelector("#createTagName");
      const newTag = newTagInput.value;
      const path = `/api/notebooks/${notebookId}/tags`;
      const requestBody = {
        name: newTag,
      };
      const response = await FetchDataHandler.fetchData(
        path,
        "POST",
        requestBody
      );
      if (response.ok) {
        const data = await response.json();
        const tag = {
          name: newTag,
          tagId: data.tagId,
        };
        newTagInput.value = "";
        tagForm.querySelector(".tagCtn").appendChild(genTagBtn(tag));
      } else {
        MessageMaker.failed("Create tag Failed.");
      }
    }

    function genTagBtn(tag) {
      const tagDiv = document.createElement("div");
      tagDiv.classList.add("tag");
      tagDiv.innerHTML = `
      <p>${tag.name}</p>
      <div class="deleteTagBtn">
        <p>Remove</p>
      </div>
      `;
      tagDiv
        .querySelector(".deleteTagBtn")
        .addEventListener("click", async (e) => {
          e.stopPropagation();
          const path = `/api/notebooks/${notebookId}/tags?tagId=${tag.tagId}`;
          const response = await FetchDataHandler.fetchData(path, "DELETE");
          if (response.ok) {
            MessageMaker.success(`Delete tag ${tag.name} success!`);
            tagDiv.remove();
          } else {
            MessageMaker.failed(`Delete tag ${tag.name} failed.`);
          }
        });
      return tagDiv;
    }
  }

  async #genCollaboratorForm(notebookId) {
    const form = document.createElement("div");
    form.classList.add("collaboratorForm");
    form.classList.add("display-none");
    form.innerHTML = `
    <h5>Collaborators</h5>
    `;

    form.appendChild(await genCollaborators(notebookId));
    form.appendChild(genInviationForm(notebookId));

    return form;

    async function genCollaborators(notebookId) {
      const collaborators = document.createElement("div");
      collaborators.classList.add("collaborators");
      const path = `/api/notebooks/${notebookId}/collaborators`;
      const resposne = await FetchDataHandler.fetchData(path, "GET");
      if (!resposne.ok) {
        MessageMaker.success("Error");
        return;
      }

      const data = await resposne.json();
      if (data.collaborators.length === 0) {
        return collaborators;
      }
      data.collaborators.forEach((collaboratorInfo) => {
        const collaborator = document.createElement("div");
        collaborator.classList.add("collaborator");
        collaborator.innerHTML = `
        <p>${collaboratorInfo.name}</p>
        <div class="deleteCollaboratorBtn" data-userEmail="${collaboratorInfo.userEmail}">
          <p>Remove</p>
        </div>
        `;

        collaborator
          .querySelector(".deleteCollaboratorBtn")
          .addEventListener("click", () => {
            deleteCollaborator(collaboratorInfo);
          });
        collaborators.appendChild(collaborator);

        async function deleteCollaborator(collaboratorInfo) {
          if (
            !DeleteAlert.renderDeleteAlertBox(
              "collaborator",
              collaboratorInfo.name
            )
          ) {
            return;
          }
          const path = `api/notebooks/${notebookId}/collaborators?userEmail=${collaboratorInfo.userEmail}`;
          const response = await FetchDataHandler.fetchData(path, "DELETE");
          if (response.ok) {
            MessageMaker.success("Collaborator removed.");
          } else {
            MessageMaker.failed("Error");
          }
        }
      });
      return collaborators;
    }

    function genInviationForm(notebookId) {
      const form = document.createElement("div");
      form.classList.add("invitationForm");
      form.innerHTML = `
      <p>Invite Collaborator</p>
      <input
        type="email"
        id="inviteEmail"
        placeholder="Email Address"
      />
      <input
        type="text"
        id="inviteMessage"
        placeholder="Invitation message"
      />
      <div id="inviteBtn">Invite</div>
      `;
      form.querySelector("#inviteBtn").addEventListener("click", () => {
        const inviteeEmail = form.querySelector("#inviteEmail").value;
        const message = form.querySelector("#inviteMessage").value;
        if (!Validator.validateEmailFormat(inviteeEmail)) {
          MessageMaker.failed("Invalid email format.");
          return;
        }
        inviteCollaborator(inviteeEmail, message, notebookId);
        form.querySelector("#inviteEmail").value = "";
        form.querySelector("#inviteMessage").value = "";
        document
          .querySelector(".collaboratorForm")
          .classList.add("display-none");
      });

      return form;

      async function inviteCollaborator(inviteeEmail, message, notebookId) {
        const path = `/api/notebooks/${notebookId}/invitations`;
        try {
          const response = await FetchDataHandler.fetchData(path, "POST", {
            inviteeEmail,
            message,
          });
          if (response.ok) {
            MessageMaker.success("Success!");
          } else if (response.status === 400) {
            MessageMaker.failed("Invalid email address");
          } else if (response.status === 409) {
            MessageMaker.warning("The invitation has been sent.");
          }
        } catch (e) {
          MessageMaker.failed("Error");
        }
      }
    }
  }
  async renderNoteCardCtn(notebookId, renderPage) {
    const noteCtn = document.querySelector(".noteCtn");
    document.querySelector(".noteCardCtn").remove();
    const noteCardCtn = await this.genNoteCardCtn(notebookId, renderPage);
    noteCtn.appendChild(noteCardCtn);
  }

  async genNoteCardCtn(notebookId, renderPage) {
    const noteCardCtn = document.createElement("div");
    noteCardCtn.classList.add("noteCardCtn");
    if (renderPage === "myNotebook" || renderPage === "coNotebook") {
      noteCardCtn.classList.add("notebookPage");
    } else {
      noteCardCtn.classList.add("notebooksPage");
    }

    const notes = await this.getNotes(notebookId);
    if (notes.length === 0) {
      return noteCardCtn;
    } else {
      notes.forEach((note) => {
        noteCardCtn.appendChild(this.genNoteCard(note, notebookId));
      });
      return noteCardCtn;
    }
  }

  async getNotes(noteboookId) {
    let notes = [];
    while (true) {
      let path = `/api/notebooks/${noteboookId}/notes?offset=${
        this.#filter.offset
      }&limit=${this.#filter.limit}&star=${this.#filter.star}&tag=${
        this.#filter.tag
      }&sortDesc=${this.#filter.sortDesc}&keyword=${this.#filter.keyword}
      `;
      try {
        const response = await FetchDataHandler.fetchData(path, "GET");
        if (!response.ok) {
          MessageMaker.failed("Get notes failed");
        }
        const data = await response.json();
        notes.push(...data.notes);
        if (!data.nextPage) {
          break;
        }
        this.#filter.offset += 20;
      } catch (e) {
        MessageMaker.failed("Get notes failed");
      }
    }
    return notes;
  }

  genNoteCard(note, notebookId) {
    const noteCard = document.createElement("a");
    noteCard.classList.add("noteCard");
    noteCard.href = `/notebooks/${notebookId}/notes/${note.noteId}`;
    noteCard.appendChild(
      genStar(note.name, note.star, notebookId, note.noteId)
    );
    noteCard.appendChild(genNoteName(note.name));
    noteCard.appendChild(genQuestion(note.question));
    noteCard.appendChild(genDeleteNoteBtn(notebookId, note.noteId, note.name));
    noteCard.appendChild(genCreateTime(note.createDate));
    return noteCard;

    function genStar(name, stared, notebookId, noteId) {
      const star = document.createElement("div");
      star.classList.add("star");
      if (stared) {
        star.classList.add("star-full");
      } else {
        star.classList.add("star-empty");
      }
      star.addEventListener("click", updateStar);
      return star;

      function updateStar(e) {
        e.preventDefault();
        e.stopPropagation();
        const path = `/api/notebooks/${notebookId}/notes/${noteId}`;
        let requestBody = {};
        if (star.classList.contains("star-empty")) {
          requestBody = {
            name,
            star: true,
          };
          star.classList.add("star-full");
          star.classList.remove("star-empty");
        } else {
          requestBody = {
            name,
            star: false,
          };
          star.classList.add("star-empty");
          star.classList.remove("star-full");
        }
        FetchDataHandler.fetchData(path, "PUT", requestBody);
      }
    }

    function genNoteName(name) {
      const nameElement = document.createElement("h5");
      nameElement.textContent = name;
      return nameElement;
    }

    function genQuestion(question) {
      const questionElement = document.createElement("p");
      questionElement.textContent = question;
      questionElement.classList.add("question");
      return questionElement;
    }

    function genDeleteNoteBtn(notebookId, noteId, name) {
      const btn = document.createElement("div");
      btn.classList.add("deleteNoteBtn");
      btn.addEventListener("click", deleteNote);
      return btn;
      function deleteNote(e) {
        e.preventDefault();
        e.stopPropagation();
        const path = `/api/notebooks/${notebookId}/notes/${noteId}`;
        DeleteAlert.renderDeleteAletBox("Note", name, path);
      }
    }
    function genCreateTime(time) {
      time = time.split(" ")[0];
      const timeElement = document.createElement("p");
      timeElement.textContent = time;
      timeElement.classList.add("createTime");
      return timeElement;
    }
  }

  genCreateNotebookBlock() {
    const initNotebookWrapper = document.createElement("div");
    initNotebookWrapper.classList.add("initNotebookWrapper");
    initNotebookWrapper.innerHTML = `
    <div class="initNotebookWrapper">
      <div class="initNotebookCtn">
        <img
          src="/icons/create-notebook.png"
          alt="create-notebook"
        />
        <div class="initCreateNotebookGroup">
          <h4>Create your first notebook!</h4>
          <div class="initBtns">
            <div id="initCreateNotebookBtn" class="no-select">
              Create notebook
            </div>
            <a href="" id="tutorials" class="no-select">Tutorials</a>
          </div>
        </div>
      </div>
    </div>
    `;

    initNotebookWrapper
      .querySelector("#initCreateNotebookBtn")
      .addEventListener("click", () => {
        CreateNotebookFormRender.renderCreateNotebookForm();
        CreateNotebookFormRender.main();
      });
    return initNotebookWrapper;
  }

  genNoCoNotebookBlock() {
    const noneElement = document.createElement("div");
    noneElement.classList.add("none");
    noneElement.textContent = "There is no notebooks.";
    return noneElement;
  }
}
