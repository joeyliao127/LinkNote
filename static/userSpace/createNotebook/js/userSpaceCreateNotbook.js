const createNotebookPage = document.querySelectorAll(".createNotebookPage");

function createNotebookInit() {
  cancelCreateForm();
  toPageOne();
  toPageTwo();
  tagListener();
  verifyEmailBtn();
  createNotebookBtnListener();
}
function cancelCreateForm() {
  const cancelBtn = document.querySelector("#cancel");
  const noteConsoleArea = document.querySelector(".main-area-noteConsole");
  const createNotebookArea = document.querySelector(
    ".main-area-createNotebook"
  );
  cancelBtn.addEventListener("click", () => {
    noteConsoleArea.classList.add("toNote");
    noteConsoleArea.classList.remove("toForm");
    createNotebookArea.classList.add("toNote");
    createNotebookArea.classList.remove("toForm");
  });
}

function toPageTwo() {
  const toPageTwoBtn = document.querySelector("#nextPage");
  toPageTwoBtn.addEventListener("click", () => {
    createNotebookPage.forEach((item) => {
      item.classList.toggle("pageOne");
      item.classList.toggle("pageTwo");
    });
  });
}

function toPageOne() {
  const toPageOneBtn = document.querySelector("#back");
  toPageOneBtn.addEventListener("click", () => {
    createNotebookPage.forEach((item) => {
      item.classList.toggle("pageOne");
      item.classList.toggle("pageTwo");
    });
  });
}

function tagListener() {
  function tagItemListener() {
    const tags = document.querySelectorAll(".tags p");
    tags.forEach((tag) => {
      tag.addEventListener("click", () => {
        tag.remove();
      });
    });
  }

  function getTag(tag) {
    console.log(`執行新增tag`);
    const p = document.createElement("p");
    if (tag.value != "") {
      p.textContent = "#" + tag.value;
      tag.value = "";
      tags.appendChild(p);
      tagItemListener();
    }
  }
  let tags = document.querySelector(".tags");
  const tag = document.querySelector("#tag");

  tag.addEventListener("keyup", (event) => {
    console.log(`執行新增tag`);
    if (event.keyCode == 13) {
      getTag(tag);
    }
  });

  const addBtn = document.querySelector("#addBtn");
  addBtn.addEventListener("click", () => {
    //這邊不能刪掉，一定要用匿名函示不然會有bug
    getTag(tag);
  });
}

function verifyEmailBtn() {
  const emailInput = document.querySelector("#invite");
  const emailVerifyBtn = document.querySelector("#addEmailBtn");
  const emailList = document.querySelector(".addEmailList");

  async function setEmail(email) {
    const count = emailList.querySelectorAll(".email-item").length;
    console.log(`count: ${count}`);
    if (count >= 4) {
      createErrorMsg("Up to 4 collaborators are allowed.");
      return 0;
    }
    if (!verifyEmailRegx(email)) {
      createErrorMsg("invalid email format");
      return 0;
    }
    const path = `/api/user/email?email=${emailInput.value}`;
    try {
      const emailInfo = await fetchData(path, "GET");
      if (emailInfo.status) {
        const div = document.createElement("div");
        const p = document.createElement("p");
        const img = document.createElement("img");
        div.classList.add("email-item");
        p.textContent = emailInput.value;
        img.setAttribute("src", "/static/resource/images/trash-white.png");
        div.appendChild(p);
        div.appendChild(img);
        div.dataset.userId = emailInfo.userId;
        emailList.appendChild(div);
        emailInput.value = "";
        emailRemovelistener();
      }
    } catch (e) {
      createErrorMsg("invalid email address");
    }

    function emailRemovelistener() {
      const emailListDiv = document.querySelectorAll(".email-item");
      console.log(emailListDiv);
      emailListDiv.forEach((emailItem) => {
        const img = emailItem.querySelector("img");
        img.addEventListener("click", () => {
          console.log(`移除email`);
          emailItem.remove();
        });
      });
    }

    function createErrorMsg(msg) {
      const errorMsg = document.createElement("p");
      errorMsg.textContent = `(${msg})`;
      errorMsg.setAttribute("style", "color: red;");
      emailList.appendChild(errorMsg);
      setTimeout(() => {
        const errorMsg = emailList.querySelector("p:last-child");
        console.log(errorMsg);
        errorMsg.remove();
      }, 3000);
    }
  }

  emailInput.addEventListener("keyup", (event) => {
    if (event.keyCode == 13) {
      setEmail(emailInput.value);
    }
  });
  emailVerifyBtn.addEventListener("click", () => {
    setEmail(emailInput.value);
  });
}

function createNotebookBtnListener() {
  const createBtn = document.querySelector("#create");
  createBtn.addEventListener("click", () => {
    const notebookName = document.querySelector("#notebookName").value;
    const description = document.querySelector("#description").value;
    const tags = document.querySelectorAll(".tags p");
    let tagList = [];
    const emails = document.querySelectorAll(".email-item");
    let emailList = [];

    tags.forEach((tag) => {
      tagList.push(tag.textContent);
    });

    emails.forEach((email) => {
      emailList.push({
        email: email.textContent,
        emailId: parseInt(email.dataset.userId),
      });
    });
    const createNBParam = {
      name: notebookName,
      tags: tagList,
      description: description,
      emails: emailList,
    };
    const path = "/api/notebooks";
    console.log(`data:`);
    console.log(createNBParam);
    fetchData(path, "POST", createNBParam);
  });
}
createNotebookInit();
