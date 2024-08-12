class DeleteAlert {
  constructor() {}

  static renderDeleteAletBox(target, name, path) {
    const main = document.querySelector("main");
    const alertBoxWrapper = document.createElement("div");
    alertBoxWrapper.classList.add("alertWrapper");
    alertBoxWrapper.innerHTML = `
    <div class="alertBox">
              <p>
                Delete ${target} "${name}" ?
              </p>
              <div class="deleteBtns">
                <div id="delete">Delete</div>
                <div id="cancelDelete">Cancel</div>
              </div>
            </div>
    `;

    genDeleteTargetBtn(path);
    genRemoveWrapperBtn();
    main.appendChild(alertBoxWrapper);

    function genDeleteTargetBtn(path) {
      const deleteBtn = alertBoxWrapper.querySelector("#delete");
      deleteBtn.addEventListener("click", () => {
        FetchDataHandler.fetchData(path, "DELETE");
        window.location.href = "/notebooks";
      });
    }

    function genRemoveWrapperBtn() {
      const cancelBtn = alertBoxWrapper.querySelector("#cancelDelete");
      cancelBtn.addEventListener("click", () => {
        alertBoxWrapper.remove();
      });
    }
  }
}
