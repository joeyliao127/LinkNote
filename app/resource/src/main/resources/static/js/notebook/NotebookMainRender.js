import Swal from "sweetalert2";
import {RequestHandler} from "@unityJS/RequestHandler";
import {MessageSender} from "@unityJS/MessageSender";
import {OwnerNotebookComponentFactory} from "@notebookJS/componentFactory/NotebookComponentFactory";
import {CollaborativeNotebookComponentFactory} from "@notebookJS/componentFactory/NotebookComponentFactory";

const $ = require("jquery");

export class NotebookMainRender {

  requestHandler;
  mainComponents;
  messageSender;
  ownerNotebookComponentFactory;
  collaborativeNotebookComponentFactory;

  constructor() {
    this.messageSender = new MessageSender();
    this.requestHandler = new RequestHandler();
    this.ownerNotebookComponentFactory = new OwnerNotebookComponentFactory();
    this.collaborativeNotebookComponentFactory = new CollaborativeNotebookComponentFactory();
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
    this.eventRegister();

    //TODO 渲染localstorage中的lastOpenNotebookId，如果沒有則渲染All notebook
    //TODO 暫時先渲染myNotebook
    this.displayMainComponent({
      displayComponentName: "myNotebookArea",
      path: "/api/notebooks?offset=0&limit=20"
    });
  }

  async displayMainComponent(displayComponent) {
    Object.entries(this.mainComponents).forEach(
        ([componentName, component]) => {
          component.addClass("display-none");
        })
    const {displayComponentName} = displayComponent;
    switch (displayComponentName) {
      case "myNotebookArea":
        const ownerNotebookPath = displayComponent['path'];
        const ownerNotebooks = await this.getNotebooks(ownerNotebookPath);
        this.renderOwnerNotebooks(ownerNotebooks);
        localStorage.setItem("lastMainComponent", displayComponentName);
        break;
      case "coNotebookArea":
        const collaborativeNotebookPath = displayComponent['path'];
        const collaborativeNotebooks = await this.getNotebooks(collaborativeNotebookPath);
        this.renderCollaborativeNotebooks(collaborativeNotebooks);
        localStorage.setItem("lastMainComponent", displayComponentName);
        break;
      case "specificOwnerNotebook":
        this.renderSpecificOwnerNotebook(displayComponent["notebookId"]);
        localStorage.setItem("lastMainComponent", displayComponentName);
        localStorage.setItem("lastOpenNotebookId", displayComponent["notebookId"]);
        break;
      case "specificCollaboratorNotebook":
        this.renderSpecificCollaborativeNotebook(displayComponent["notebookId"]);
        localStorage.setItem("lastMainComponent", displayComponentName);
        localStorage.setItem("lastOpenNotebookId", displayComponent["notebookId"]);
        break;
      case "invitationsForm":
        this.renderInvitationForm();
        break;
      case "settingForm":
        break;
      case "createNotebookForm":
        break;
      default:
        break;
    }
    this.mainComponents[displayComponentName].removeClass("display-none");
  }

  getNotebooks = async (path) => {
    const response = await this.requestHandler.sendRequestWithToken(path, "GET", null);

    if(!response.ok) {
      this.messageSender.error("Get notebooks failed");
    }

    const data = await response.json();
    return data.notebooks;
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
      this.displayMainComponent({
        displayComponentName: "createNotebookForm"
      });
    });
    $(".js_search_owner_notebook input").on("keypress", (e) => {
      const input = $(".js_search_owner_notebook input");
      const keyword = input.val();
      if ((e.key === "Enter") && keyword) {
        this.displayMainComponent({
          displayComponentName: "myNotebookArea",
          path: `/api/notebooks?keyword=${keyword}&offset=0&limit=20`
        });
        input.val('');
      }
    })

    $(".js_search_owner_notebook img").on('click', () => {
      const input = $(".js_search_owner_notebook input");
      const keyword = input.val();
      if(keyword) {
        this.displayMainComponent({
          displayComponentName: "myNotebookArea",
          path: `/api/notebooks?keyword=${keyword}&offset=0&limit=20`
        });
        input.val('');
      }
    })

    $(".js_search_collaborate_notebook input").on("keypress", (e) => {
      const input = $(".js_search_collaborate_notebook input");
      const keyword = input.val();
      if ((e.key === "Enter") && keyword) {
        this.displayMainComponent({
          displayComponentName: "coNotebookArea",
          path: `/api/coNotebooks?keyword=${keyword}&offset=0&limit=20`
        });
        input.val("");
      }
    })
    $(".js_search_collaborate_notebook img").on('click', () => {
      const input = $(".js_search_collaborate_notebook input");
      const keyword = input.val();
      if(keyword) {
        this.displayMainComponent({
          displayComponentName: "coNotebookArea",
          path: `/api/coNotebooks?keyword=${keyword}&offset=0&limit=20`
        });
        input.val("");
      }
    })
  }

  submitCreateNotebookForm = async () => {
    const notebookName = $(".js_new_notebook_name_input").val();
    if (!notebookName) {
      this.messageSender.error("Notebook name cannot be empty.");
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
      //todo 記得刪除，data回傳格式為
      // {
      //   "result": true,
      //     "notebookId": "NBe6d72aae2fe2e258a87d7d17f1c19d4d"
      // }
      const data = await response.json();

      Swal.fire({
        title: 'Success!',
        text: 'Create notebook success!',
        icon: 'success',
        showConfirmButton: true,
        timer: 5000
      }).then(() => {
        this.clearCreateNotebookFormInput();
        // TODO 暫時先渲染main，未來要改為渲染specific notebookId
        this.displayMainComponent({
          displayComponentName: "specificOwnerNotebook"
        });
      });
    } else {
      this.messageSender.error("Create notebook failed. Please try again.");
    }
  }

  handleCreateNewTag = () => {
    const inputElement = $('.js_new_tag_input')
    const tagName = inputElement.val();
    if (this.isDuplicateTagOrNull(tagName)) {
      this.messageSender.error("Tag name already exists.");
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
    this.clearCreateNotebookFormInput();
    // todo 取消後應該要渲染上一本開的notebook，這邊暫時先渲染myNotebook
    this.displayMainComponent({
      displayComponentName: "myNotebookArea"
    });
  }

  clearCreateNotebookFormInput() {
    $(".js_new_notebook_name_input").val('');
    $(".js_new_tag").remove();
    $(".js_new_notebook_description_input").val('');
  }

  renderOwnerNotebooks = (notebooks) => {
    const myNotebookArea = $(".js_myNotebook_area");
    $(`.js_owner_notebook_ctn`).empty();

    if(notebooks.length === 0) {
      this.displayCreateNotebookElement();
      return;
    }
    $('.js_myNotebook_top').show();
    this.hideCreateNotebookElement();
    const notebookContainer = this.ownerNotebookComponentFactory.generateOwnerNotebooks(notebooks);
    myNotebookArea.append(notebookContainer);
  }

  renderCollaborativeNotebooks = (notebooks) => {
    const collaborativeNotebookArea = $(".js_coNotebook_area");
    $(".js_collaborative_notebook_ctn").empty();

    if(notebooks.length === 0) {
      $('.js_none_collaborative_notebook').show();
      return;
    }
    $('.js_coNotebook_top').show();
    $('.js_none_collaborative_notebook').hide();
    // this.registerSearchCollaborativeNotebooksEvent($('.js_search_collaborate_notebook'));
    const notebookContainer = this.collaborativeNotebookComponentFactory.generateCollaborativeNotebooks(notebooks);
    collaborativeNotebookArea.append(notebookContainer);
  }

  renderSpecificOwnerNotebook = (notebookId) => {
    this.displayMainComponent({
      displayComponentName: "specificNotebookArea"
    });
  }

  renderSpecificCollaborativeNotebook = () => {

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
      this.messageSender.error("Fetch invitations failed. Please try again.");
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
        this.messageSender.error("Accept invitation failed. Please try again.");
      }
    })

    const denyBtn = invitationElement.find(".denyInvitation");
    denyBtn.click( async () => {
      const result = await this.updateNotebookReceivedInvitation(invitation, false);
      if(result) {
        const message = "Reject invitation success!";
        this.updateInvitationSuccessCallback(invitationElement, message);
      } else {
        this.messageSender.error("Accept invitation failed. Please try again.");
      }
    })

    return invitationElement;
  }

  updateInvitationSuccessCallback = (invitationElement, message) => {
    invitationElement.remove();
    this.messageSender.success(message);
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
        this.messageSender.success("Withdraw invitation success!");
      } else {
        this.messageSender.error("Withdraw invitation failed. Please try again.");
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
      this.messageSender.error("Username or password cannot be empty.");
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
      this.messageSender.error("Update user profile failed. Please try again.");
    }
  }

  displayCreateNotebookElement() {
    $('.js_init_create_notebook_wrapper').show();
    $('.notebooksTop').hide();
  }

  hideCreateNotebookElement() {
    $('.js_init_create_notebook_wrapper').hide();
  }

  renderNotebookArea(params) {
    this.displayMainComponent();
  }
}

