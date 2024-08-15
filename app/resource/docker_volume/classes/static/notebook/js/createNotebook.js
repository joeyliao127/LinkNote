class CreateNotebookFormRender {
  constructor() {}

  #main() {
    this.#createNotebookListener();
    this.#cancelFormBtnListener();
    this.#createNotebookTagInputListener();
    this.#createNotebookTagBtnListener();
  }

  #createNotebookTagBtnListener() {
    document
      .querySelector("#addTag")
      .addEventListener("click", this.#generateTags);
  }

  #createNotebookTagInputListener() {
    document.querySelector("#tag").addEventListener("keypress", (e) => {
      if (e.key === "Enter") {
        this.#generateTags();
      }
    });
  }

  #cancelFormBtnListener() {
    document.querySelector("#cancel").addEventListener("click", () => {
      window.location.href = window.location.href;
    });
  }

  #generateTags() {
    const tag = document.createElement("p");
    const text = document.querySelector("#tag").value;
    if (!text) {
      return;
    }
    tag.textContent = text;
    document.querySelector("#tag").value = "";
    tag.addEventListener("click", () => {
      tag.remove();
    });
    document.querySelector(".tags").appendChild(tag);
  }

  #createNotebookListener() {
    const submitBtn = document.querySelector("#submit");
    submitBtn.addEventListener("click", this.#createNotebook);
  }

  async #createNotebook() {
    const tags = [];
    document.querySelectorAll(".tags p").forEach((item) => {
      tags.push({
        name: item.textContent,
      });
    });
    const notebookName = document.querySelector("#notebookName").value;
    if (!notebookName) {
      MessageMaker.warning("Notebook name is required.");
      return;
    }
    const requestBody = {
      name: notebookName,
      description: document.querySelector("#description").value,
      tags: tags,
    };

    console.log(requestBody);
    const response = await FetchDataHandler.fetchData(
      "/api/notebooks",
      "POST",
      requestBody
    );

    if (response.status == 200) {
      window.location.href = window.location.href;
    }
  }

  renderCreateNotebookForm() {
    const createNotebookWrapper = document.createElement(
      "createNotebookWrapper"
    );
    createNotebookWrapper.classList.add("createNotebookWrapper");
    createNotebookWrapper.innerHTML = `
    <h3>Create Notebook</h3>
    <div class="createNotebookForm">
      <div class="inputs">
        <label for="notebookName">Notebook Name</label>
        <input
          type="text"
          name="notebookName"
          id="notebookName"
          placeholder="Name"
        />
        <label for="description">Description (Optional)</label>
        <input
          type="text"
          name="description"
          id="description"
          placeholder="What is the purpose of the notebook?"
        />
        <label for="tag">Tags</label>
        <div class="tagInput">
          <input type="text" name="tag" id="tag" placeholder="Tag Name" />
          <button id="addTag">Add</button>
        </div>
        <div class="tags"></div>
      </div>
  
      <div class="flex createNotebookBtns">
        <div id="cancel" class="no-select">Cancel</div>
        <div id="submit" class="no-select">Submit</div>
      </div>
    </div>`;
    ReRenderElement.reRenderMain(createNotebookWrapper);
    this.#main();
  }
}
