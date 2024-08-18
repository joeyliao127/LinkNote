import {NotebookComponentGenerator} from "@notebookJS/NotebookComponentGenerator";
import iziToast from "izitoast";
import 'izitoast/dist/css/iziToast.min.css';
import Swal from "sweetalert2";

const $ = require("jquery");

export class NotebookMainRender {

  requestHandler;
  mainComponents;
  notebookComponentGenerator;

  constructor(requestHandler, notebookComponentGenerator) {
    this.notebookComponentGenerator = new NotebookComponentGenerator();
    this.notebookComponentGenerator = notebookComponentGenerator;
    this.requestHandler = requestHandler;
    this.mainComponents = {
      "createNotebookForm": $(".js_create_notebook_wrapper"),
      // "myNotebookArea": $(""),
      // "coNotebookArea": $(""),
      // "invitationsForm": $(""),
      "settingForm": $(".js_user_profile_wrapper"),
    }
  }

  async init() {
    const lastMainComponent = localStorage.getItem("lastMainComponent");
    if (lastMainComponent) {
      this.displayMainComponent(lastMainComponent);
    } else {
      this.displayMainComponent("settingForm");
    }
    this.eventRegister();

    //TODO 渲染localstorage中的lastOpenNotebookId，如果沒有則渲染All notebook
  }

  displayMainComponent(displayComponentName) {
    Object.entries(this.mainComponents).forEach(
        ([componentName, component]) => {
          if (componentName === displayComponentName) {
            component.show();
          } else {
            component.hide();
          }
        })
    localStorage.setItem("lastMainComponent", this.mainComponents.createNotebook);
  }

  eventRegister() {
    $(".js_update_user_profile_btn").on('click', this.submitSettingForm);
    $(".js_create_notebook_submit_btn").on('click', this.submitCreateNotebookForm);
    $(".js_add_tag_btn").on('click', this.handleCreateNewTag);
    $(".js_new_tag_input").on('keypress', (e) => {
      if(e.key === "Enter") {
        this.handleCreateNewTag();
      }
    })
  }

  submitCreateNotebookForm = async () => {
    const notebookName = $(".js_new_notebook_name_input").val();
    if(!notebookName) {
      iziToast.error({
        "title": "Error",
        "message": "Notebook name cannot be empty.",
        "position": "topRight"
      })
      return;
    }

    let tags = [];
    $(".js_new_tag").each((index, element) => {
      tags.push({
        name: element.textContent
      })
    })

    const requestBody = {
      name: notebookName,
      description: $(".js_new_notebook_description_input").val(),
      tags: tags
    }

    const response = await this.requestHandler.sendRequestWithToken("/api/notebooks", "POST", requestBody);

    if(response.ok) {
      const data = await response.json();
      // TODO 渲染main，寫在.then裡面
      Swal.fire({
        title: 'Success!',
        text: 'Create notebook success!',
        icon: 'success',
        showConfirmButton: true,
        timer: 1500
      }).then();
    } else {
      iziToast.error({
        "title": "Error",
        "message": "Create notebook failed. Please try again.",
        "position": "topRight"
      })
    }
  }
  submitSettingForm = async () => {
    const username = $(".js_setting_username").val();
    const password = $(".js_setting_password").val();
    // 驗證username & password
    if (!(username && password)) {
      iziToast.error({
        "title": "Error",
        "message": "Username or password cannot be empty.",
        "position": "topRight"
      })
      return;
    }
    //TODO 開發用的url，正式機要移除 domain ;
    const domain = "http://127.0.0.1:8080";
    const path = domain + "/api/user";
    const email = localStorage.getItem("email");
    const requestBody = {username, password, email};
    const response = await this.requestHandler.sendRequestWithToken(path, "PUT", requestBody);

    if(response.ok) {
      localStorage.setItem("username", username);
      Swal.fire({
        title: 'Success!',
        text: 'Update user profile success! Please sign out and sign in again.',
        icon: 'success',
        confirmButtonText: 'OK'
      }).then((result) => {
        if (result.isConfirmed) {
          localStorage.removeItem("token");
          window.location.href = "/";
        }
      })
    } else {
      iziToast.error({
        "title": "Error",
        "message": "Update user profile failed!",
        "position": "topRight"
      })
    }
  }

  handleCreateNewTag = () => {
    const inputElement = $('.js_new_tag_input')
    const tagName = inputElement.val();
    if(this.isDuplicateTagOrNull(tagName)){
      iziToast.error({
        "title": "Error",
        "message": "Tag name already exists.",
        "position": "topRight"
      })
      inputElement.val('');
      return;
    }
    // 清空input
    inputElement.val('');
    const newTagElement = this.generateNotebookTagElement(tagName);
    $('.tags').append(newTagElement);
  }

  generateNotebookTagElement = (tagName) => {
    const newTagElement = $(`<p class="js_new_tag">${tagName}</p>`);
    newTagElement.on('click', () => {
      newTagElement.remove();
    })
    return newTagElement;
  }

  //檢查tag是否有重複
  isDuplicateTagOrNull = (newTagName) => {
    if(newTagName === '') {
      return true;
    }

    let isDuplicate = false;
    //檢查是否有重複的tag
    $('.js_new_tag').each((index, element) => {
      if (newTagName === element.textContent) {
        isDuplicate = true;
        return false;
      }
    })

    return isDuplicate;
  }

}