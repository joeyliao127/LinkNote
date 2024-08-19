import {NotebookComponentGenerator} from "@notebookJS/NotebookComponentGenerator";
import iziToast from "izitoast";
import 'izitoast/dist/css/iziToast.min.css';
import Swal from "sweetalert2";
import {RequestHandler} from "@unityJS/RequestHandler";

const $ = require("jquery");

export class NotebookMainRender {

  requestHandler;
  mainComponents;
  notebookComponentGenerator;

  constructor() {
    this.notebookComponentGenerator = new NotebookComponentGenerator();
    this.requestHandler = new RequestHandler();
    this.mainComponents = {
      "createNotebookForm": $(".js_create_notebook_wrapper"),
      "myNotebookArea": $(".js_myNotebook_area"),
      "coNotebookArea": $(".js_coNotebook_area"),
      "invitationsForm": $(".js_invitation_wrapper"),
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
    //TODO 暫時先渲染myNotebook
    this.displayMainComponent("myNotebookArea");
  }

  displayMainComponent(displayComponentName) {
    Object.entries(this.mainComponents).forEach(
        ([componentName, component]) => {
          //todo 這個if else要移除，全部先hide後，在根據componentName來show
          //todo 用switch case 判斷 component name，除了mynotebook和conotebook，其餘的都走default
          //todo default是渲染lastOpenNotebookId的notebook，如果沒有則渲染myNotebook
          if (componentName === displayComponentName) {
            component.show();
          } else {
            component.hide();
          }
        })

    localStorage.setItem("lastMainComponent", displayComponentName);
  }

  eventRegister() {
    $(".js_update_user_profile_btn").on('click', this.submitSettingForm);
    $(".js_create_notebook_submit_btn").on('click', this.submitCreateNotebookForm);
    $(".js_cancel_create_notebook_btn").on('click', this.cancelCreateNotebookForm);
    $(".js_add_tag_btn").on('click', this.handleCreateNewTag);
    $(".js_new_tag_input").on('keypress', (e) => {
      if (e.key === "Enter") {
        this.handleCreateNewTag();
      }
    })
    $(".js_init_create_notebook_btn").on('click', () => {
      this.displayMainComponent("createNotebookForm");
    });
  }

  submitCreateNotebookForm = async () => {
    const notebookName = $(".js_new_notebook_name_input").val();
    if (!notebookName) {
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

    const response = await this.requestHandler.sendRequestWithToken(
        "/api/notebooks", "POST", requestBody);

    if (response.ok) {
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

  handleCreateNewTag = () => {
    const inputElement = $('.js_new_tag_input')
    const tagName = inputElement.val();
    if (this.isDuplicateTagOrNull(tagName)) {
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
    if (newTagName === '') {
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

  // 清空create notebook表單內容
  cancelCreateNotebookForm = () => {
    $(".js_new_notebook_name_input").val('');
    $(".js_new_tag").remove();
    $(".js_new_notebook_description_input").val('');
    // todo 取消後應該要渲染上一本開的notebook，這邊暫時先渲染myNotebook
    this.displayMainComponent("myNotebookArea");
  }

  // 渲染共編邀請表單，每次渲染都會重新fetch，所以要將表單中原有的html移除
  renderInvitationForm = () => {
    $('.js_received_invitation').remove();
    $('.js_sent_invitation').remove();
    this.handleReceivedInvitations();
    this.handleSentInvitations();
  }

  handleReceivedInvitations = async () => {
    const response = await this.requestHandler.sendRequestWithToken(
        "/api/invitations/received-invitations?offset=0&limit=20", "GET", null);

    if (!response.ok) {
      iziToast.error({
        "title": "Error",
        "message": "Fetch invitations failed. Please try again.",
        "position": "topRight"
      })
    }

    const data = await response.json();
    const invitations = data.invitations;

    if (invitations.length > 0) {
      $('.js_received_none').hide();
      $('.js_received_header').show();
      invitations.forEach((invitation) => {
        $('.js_received_table').append(
            this.genReceivedInvitationTr(invitation));
      })
    }
  }

  genReceivedInvitationTr(invitation) {
    const createDate = invitation.createDate.split(" ");
    const invitationElement = $(
        `<tr class="js_received_invitation">
          <td>${invitation.inviterName}</td>
          <td>${invitation.notebookName}</td>
          <td>${createDate[0]}</td>
          <td>${invitation.message}</td>
          <td>
            <button class="acceptInvitation">Accept</button>
            <button class="denyInvitation">Deny</button>
          </td>
        </tr>`
    );

    const acceptBtn = invitationElement.find(".acceptInvitation");
    acceptBtn.click(async () => {
      const result =await this.updateNotebookReceivedInvitation(invitation, true);
      if(result) {
        const message = "Accept invitation success!";
        this.updateInvitationSuccessCallback(invitationElement, message);
      } else {
        iziToast.error({
          "title": "Error",
          "message": "Accept invitation failed. Please try again.",
          "position": "topRight"
        })
      }
    })

    const denyBtn = invitationElement.find(".denyInvitation");
    denyBtn.click( async () => {
      const result = await this.updateNotebookReceivedInvitation(invitation, false);
      if(result) {
        const message = "Reject invitation success!";
        this.updateInvitationSuccessCallback(invitationElement, message);
      } else {
        iziToast.error({
          "title": "Error",
          "message": "Accept invitation failed. Please try again.",
          "position": "topRight"
        })
      }
    })

    return invitationElement;
  }

  updateInvitationSuccessCallback = (invitationElement, message) => {
    invitationElement.remove();
    iziToast.success({
      "title": "Success",
      "message": message,
      "position": "topRight"
    })
    if(!$('.js_received_invitation').length) {
      $('.js_received_none').show();
      $('.js_received_header').hide();
    }
  }

  // 接受筆記本共編邀請
  updateNotebookReceivedInvitation = async (invitation, isAccept) => {
    const path = `/api/invitations/received-invitation`;
    const response = await this.requestHandler.sendRequestWithToken(path, "PUT",
        {notebookId: invitation.notebookId, isAccept});
    return response.ok;
  }

  handleSentInvitations = async () => {
    const response = await this.requestHandler.sendRequestWithToken(
        "/api/invitations/sent-invitations?offset=0&limit=20", "GET", null);
    const data = await response.json();
    const invitations = data.invitations;
    if (invitations.length > 0) {
      $('.js_sent_invitation_none').hide();
      $('.js_sent_invitation_header').show();
      invitations.forEach((invitation) => {
        $('.js_sent_table').append(this.genSentInvitationTr(invitation));
      })
    }
  }

  genSentInvitationTr = (invitation) => {
    const createDate = invitation.createDate.split(" ");
    const invitationElement = $(
        `<tr class="js_sent_invitation">
          <td>${invitation.inviteeName}</td>
          <td>${invitation.notebookName}</td>
          <td>${createDate[0]}</td>
          <td>${invitation.message}</td>
          <td>
            <button class="deleteInvitation js_withdraw_invitation_btn">Delete</button>
          </td>
        </tr>`
    );

    const withDrawInvitationBtn = invitationElement.find('.js_withdraw_invitation_btn');
    withDrawInvitationBtn.on('click', async () => {
      const result = await this.withdrawInvitation(invitation);
      if(result) {
        invitationElement.remove();
        iziToast.success({
          "title": "Success",
          "message": "Withdraw invitation success!",
          "position": "topRight"
        })
      } else {
        iziToast.error({
          "title": "Error",
          "message": "Withdraw invitation failed. Please try again.",
          "position": "topRight"
        })
      }

      if(!$('.js_sent_invitation').length) {
        $('.js_sent_invitation_none').show();
        $('.js_sent_invitation_header').hide();
      }
    });

    return invitationElement;
  }

  // 退回筆記本共編邀請
  withdrawInvitation = async (invitation) => {
    const path = `/api/notebooks/${invitation.notebookId}/invitations`;
    const response = await this.requestHandler.sendRequestWithToken(path, "DELETE", null);
    return response.ok;
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
    const response = await this.requestHandler.sendRequestWithToken(path, "PUT",
        requestBody);

    if (response.ok) {
      localStorage.setItem("username", username);
      Swal.fire({
        title: 'Success!',
        text: 'Update user profile success! Please sign out and sign in again.',
        icon: 'success',
        confirmButtonText: 'OK'
      }).then((result) => {
        if (result.isConfirmed) {
          localStorage.clear();
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

}