import {RequestHandler} from "@unityJS/RequestHandler";
const $ = require("jquery");
export class DeleteAlert {

  requestHandler = new RequestHandler();
  renderDeleteAlertBox(target, name, confirmCallback) {
    const alertBoxWrapper = $(`
        <div class="alertWrapper">
          <div class="alertBox">
            <p>
              Delete ${target} "${name}" ?
            </p>
            <div class="deleteBtns">
              <div id="delete">Delete</div>
              <div id="cancelDelete">Cancel</div>
            </div>
          </div>
        </div>
    `);
    $('main').append(alertBoxWrapper);


    const deleteBtn = alertBoxWrapper.find("#delete");
    deleteBtn.on('click', () => {
      confirmCallback();
      alertBoxWrapper.remove();
    });

    const cancelBtn = alertBoxWrapper.find("#cancelDelete");
    cancelBtn.click(() => {
      alertBoxWrapper.remove();
    });
  }
}