class SideBarRender {
  constructor() {
    this.#notebookRender.renderMyNotebooks();
    this.genNotebookBtns("myNotebooks");
    this.setUsernameAndEmail();
    this.genCreateNotebookBtnListner();
    this.myNotebookBtnListner();
    this.coNotebookBtnListner();
    this.invitationPageBtnListner();
    this.settingPageBtnListner();
    this.signoutBtnListener();
  }

  #notebookRender = new NotebookRender();
  #createNotebookFrom = new CreateNotebookFormRender();
  #myNotebookBtnOffset = 0;
  #myNotebookBtnLimit = 20;
  #coNotebookBtnOffset = 0;
  #coNotebookBtnLimit = 20;
  #notebooks = {};
  #selectedNotebookBtns = {};
  #selectedBtn = {
    myNotebookBtn: document.querySelector(".sideBarMyNotebookArea .sideBarBtn"),
    coNotebookBtn: document.querySelector(".sideBarCoNotebookArea .sideBarBtn"),
    invitationBtn: document.querySelector(".invitationsBtn"),
    settingBtn: document.querySelector(".settingBtn"),
    selectedElement: null,
  };

  async genNotebookBtns(renderPage) {
    const coNotebookBtnsCtn = document.querySelector(
      "#sideBar-coNotebookBtnCtn"
    );
    const myNotebookBtnsCtn = document.querySelector(
      "#sideBar-myNotebookBtnCtn"
    );
    myNotebookBtnsCtn.innerHTML = "";
    coNotebookBtnsCtn.innerHTML = "";
    let path;
    let notebookBtnsCtn;
    if (renderPage === "myNotebooks") {
      this.#renderSelectBtn("myNotebookBtn");
      notebookBtnsCtn = myNotebookBtnsCtn;
      path = `/api/notebooks?offset=${this.#myNotebookBtnOffset}&limit=${
        this.#myNotebookBtnLimit
      }`;
    } else {
      this.#renderSelectBtn("coNotebookBtn");
      notebookBtnsCtn = coNotebookBtnsCtn;
      path = `/api/coNotebooks?offset=${this.#coNotebookBtnOffset}&limit=${
        this.#coNotebookBtnLimit
      }`;
    }
    this.#notebooks = await this.#getNotebooks(path);
    if (this.#notebooks.length != 0) {
      this.#notebooks.forEach((notebook) => {
        notebookBtnsCtn.appendChild(this.#genNotebookBtn(notebook, renderPage));
      });
    }
  }

  async setUsernameAndEmail() {
    const response = await FetchDataHandler.fetchData("/api/user/info", "GET");
    const data = await response.json();
    document.querySelector("#username").textContent = data.username;
    document.querySelector("#userEmail").textContent = data.email;
    localStorage.setItem("email", data.email);
    localStorage.setItem("username", data.username);
  }

  genCreateNotebookBtnListner() {
    document
      .querySelector("#createNotebookBtn")
      .addEventListener("click", () => {
        this.#createNotebookFrom.renderCreateNotebookForm();
      });
  }

  myNotebookBtnListner() {
    document
      .querySelector(".sideBarMyNotebookArea .sideBarBtn")
      .addEventListener("click", () => {
        this.#notebookRender.resetFilter();
        this.#notebookRender.renderMyNotebooks();
        if (this.#selectedBtn.selectedElement == "myNotebookBtn") {
          this.#renderSelectNotebookBtn("none");
          return;
        }
        this.#renderSelectBtn("myNotebookBtn");

        this.genNotebookBtns("myNotebooks");
      });
  }

  coNotebookBtnListner() {
    document
      .querySelector(".sideBarCoNotebookArea .sideBarBtn")
      .addEventListener("click", () => {
        this.#notebookRender.resetFilter();
        if (this.#selectedBtn.selectedElement == "coNotebookBtn") {
          this.#renderSelectNotebookBtn("");
          return;
        }
        this.#renderSelectBtn("coNotebookBtn");
        this.#notebookRender.renderCoNotebooks();
        this.genNotebookBtns("coNotebooks");
      });
  }

  invitationPageBtnListner() {
    document.querySelector(".invitationsBtn").addEventListener("click", () => {
      if (this.#selectedBtn.selectedElement == "invitationBtn") {
        return;
      }
      genInvitationTable();
      this.#renderSelectBtn("invitationBtn");
    });
    async function genInvitationTable() {
      const invitationWrapper = document.createElement("div");
      invitationWrapper.classList.add("invitationWrapper");
      invitationWrapper.innerHTML = `
      <section class="invitationTable" id="receivedInvitaionTable">
      <h4>Pending Received Invitations</h4>
      <table id="received-table">
        <tr id="received-none">
          <th>none</th>
        </tr>
        <tr  id="received-header" class="display-none" >
          <th>Inviter</th>
          <th>Notebook Name</th>
          <th>Create Date</th>
          <th>Message</th>
          <th>Confirm</th>
        </tr>
      </table>
    </section>
    <section class="invitaionTable" id="sentInvitaionTable">
      <section class="invitationTable" id="receivedInvitaionTable">
        <h4>Pending Sent Invitations</h4>
        <table id="sent-table">
          <tr id="sent-none" >
            <th>none</th>
          </tr>
          <tr  id="sent-header" class="display-none" >
            <th>Invitee</th>
            <th>Notebook Name</th>
            <th>Create Date</th>
            <th>Message</th>
            <th>Confirm</th>
          </tr>
          
        </table>
      </section>
    </section>
      `;
      const receviedResponse = await FetchDataHandler.fetchData(
        "/api/invitations/received-invitations?offset=0&limit=20",
        "GET"
      );
      const receivedData = await receviedResponse.json();
      const receivedInvitations = receivedData.invitations;
      const receivedTable = invitationWrapper.querySelector("#received-table");
      if (receivedInvitations.length !== 0) {
        invitationWrapper
          .querySelector("#received-none")
          .classList.add("display-none");
        invitationWrapper
          .querySelector("#received-header")
          .classList.remove("display-none");
        receivedInvitations.forEach((invitation) => {
          receivedTable.appendChild(genReceivedInvitationTr(invitation));
        });
      }

      const sentResponse = await FetchDataHandler.fetchData(
        "/api/invitations/sent-invitations?offset=0&limit=20",
        "GET"
      );
      const sentData = await sentResponse.json();
      const sentInvitations = sentData.invitations;
      const sentTable = invitationWrapper.querySelector("#sent-table");
      if (sentInvitations.length !== 0) {
        invitationWrapper
          .querySelector("#sent-none")
          .classList.add("display-none");
        invitationWrapper
          .querySelector("#sent-header")
          .classList.remove("display-none");
        sentInvitations.forEach((invitation) => {
          sentTable.appendChild(genSentInvitationTr(invitation));
        });
      }

      ReRenderElement.reRenderMain(invitationWrapper);
    }

    function genReceivedInvitationTr(invitation) {
      const tr = document.createElement("tr");
      const createDate = invitation.createDate.split(" ");
      tr.innerHTML = `
      <tr>
      <td>${invitation.inviterName}</td>
      <td>${invitation.notebookName}</td>
      <td>${createDate[0]}</td>
      <td>${invitation.message}</td>
      <td>
        <button class="acceptInvitation">Accept</button>
        <button class="denyInvitation">Deny</button>
      </td>
    </tr>
      `;
      const acceptBtn = tr.querySelector(".acceptInvitation");
      acceptBtn.addEventListener("click", async () => {
        const path = `/api/invitations/received-invitation`;
        const response = await FetchDataHandler.fetchData(path, "PUT", {
          notebookId: invitation.notebookId,
          isAccept: true,
        });
        if (response.ok) {
          tr.remove();
          MessageMaker.success("success!");
        } else {
          MessageMaker.failed("failed.");
        }
      });

      const denyBtn = tr.querySelector(".denyInvitation");
      denyBtn.addEventListener("click", async () => {
        const path = `/api/invitations/received-invitation`;
        const response = await FetchDataHandler.fetchData(path, "PUT", {
          notebookId: invitation.notebookId,
          isAccept: false,
        });
        if (response.ok) {
          MessageMaker.success("success!");
          tr.remove();
        } else {
          MessageMaker.failed("failed.");
        }
      });
      return tr;
    }

    function genSentInvitationTr(invitation) {
      const tr = document.createElement("tr");
      const createDate = invitation.createDate.split(" ");
      tr.innerHTML = `
      <tr>
      <td>${invitation.inviteeName}</td>
      <td>${invitation.notebookName}</td>
      <td>${createDate[0]}</td>
      <td>${invitation.message}</td>
      <td>
        <button class="deleteInvitation">Delete</button>
      </td>
    </tr>
      `;
      const delBtn = tr.querySelector(".deleteInvitation");
      delBtn.addEventListener("click", async () => {
        const path = `/api/notebooks/${invitation.notebookId}/invitations`;
        const response = await FetchDataHandler.fetchData(path, "DELETE");
        if (response.ok) {
          MessageMaker.success("removed!");
          tr.remove();
        } else {
          MessageMaker.failed("failed.");
        }
      });
      return tr;
    }
  }

  settingPageBtnListner() {
    document.querySelector(".settingBtn").addEventListener("click", () => {
      if (this.#selectedBtn.selectedElement == "settingBtn") {
        return;
      }
      const userProfile = document.createElement("section");
      userProfile.classList.add("userProfileWrapper");
      userProfile.innerHTML = `
      <h4>Member Profile</h4>
            <div class="userProfileForm">
              <div class="userProfile">
                <p class="title">Email</p>
                <p id="email">${localStorage.getItem("email")}</p>
              </div>
              <div class="userProfile">
                <label class="title" for="usernmae">Username</label>
                <input type="text" id="username" value="${localStorage.getItem(
                  "username"
                )}" />
              </div>
              <div class="userProfile">
                <label class="title" for="password">Password</label>
                <input
                  type="password"
                  name="password"
                  id="password"
                  placeholder="passowrd"
                />
              </div>
              <button id="userProfileBtn">Save</button>
            </div>
      `;

      userProfile
        .querySelector("#userProfileBtn")
        .addEventListener("click", async () => {
          if (
            !userProfile.querySelector("#password").value ||
            !userProfile.querySelector("#username").value
          ) {
            MessageMaker.warning("Username or password is null.");
            return;
          }
          const requestBody = {
            username: userProfile.querySelector("#username").value,
            password: userProfile.querySelector("#password").value,
            email: localStorage.getItem("email"),
          };
          const path = `/api/user`;
          const response = await FetchDataHandler.fetchData(
            path,
            "PUT",
            requestBody
          );
          if (response.ok) {
            window.location.href = window.location.href;
          } else {
            MessageMaker.failed("Error.");
          }
        });
      ReRenderElement.reRenderMain(userProfile);

      this.#renderSelectBtn("settingBtn");
    });
  }

  signoutBtnListener() {
    document.querySelector(".signoutBtn").addEventListener("click", signout);
    function signout() {
      localStorage.removeItem("token");
      location.href = "/";
    }
  }

  #renderSelectBtn(target) {
    this.#selectedBtn.myNotebookBtn.classList.remove("selected");
    this.#selectedBtn.coNotebookBtn.classList.remove("selected");
    this.#selectedBtn.invitationBtn.classList.remove("selected");
    this.#selectedBtn.settingBtn.classList.remove("selected");
    const element = this.#selectedBtn[target];
    element.classList.add("selected");
    this.#selectedBtn.selectedElement = target;
  }

  #genNotebookBtn(notebook, renderPage) {
    const element = document.createElement("p");
    element.textContent = notebook.name;
    element.dataset.description = notebook.description;
    element.dataset.id = notebook.id;
    this.#selectedNotebookBtns[notebook.id] = element;
    element.addEventListener("click", () => {
      this.#notebookRender.resetFilter();
      this.#notebookRender.resetTagFilter();
      if (renderPage === "myNotebooks") {
        this.#renderSelectBtn("myNotebookBtn");
        this.#notebookRender.renderNotebook(notebook);
      } else {
        this.#renderSelectBtn("coNotebookBtn");
        this.#notebookRender.renderCoNotebook(notebook);
      }
      this.#renderSelectNotebookBtn(notebook.id);
    });
    return element;
  }

  #getNotebooks = async function (path) {
    const response = await FetchDataHandler.fetchData(path, "GET");
    if (response.ok) {
      const notebooks = await response.json();

      return notebooks.notebooks;
    } else {
      MessageMaker.failed("Error");
    }
  };

  #renderSelectNotebookBtn(notebookId) {
    console.log(`執行select`);
    for (const [key, value] of Object.entries(this.#selectedNotebookBtns)) {
      if (key === notebookId) {
        value.classList.add("selected666");
      } else {
        value.classList.remove("selected666");
      }
    }
  }
}

const sideBarRender = new SideBarRender();
