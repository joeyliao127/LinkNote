import Swal from "sweetalert2";
import {RequestHandler} from "@unityJS/RequestHandler";
import {MessageSender} from "@unityJS/MessageSender";
import {
  CollaborativeNotebookComponentFactory,
  OwnerNotebookComponentFactory,
  SpecificCollaborativeNotebookComponentFactory,
  SpecificOwnerNotebookComponentFactory
} from "@notebookJS/componentFactory/NotebookComponentFactory";

const $ = require("jquery");

export class NotebookMainRender {

  requestHandler;
  mainComponents;
  messageSender;
  ownerNotebookFactory;
  collaborativeNotebookFactory;
  specificOwnerNotebookFactory;
  specificCollaborativeNotebookFactory;


  constructor() {
    this.messageSender = new MessageSender();
    this.requestHandler = new RequestHandler();
    this.ownerNotebookFactory = new OwnerNotebookComponentFactory();
    this.collaborativeNotebookFactory = new CollaborativeNotebookComponentFactory();
    this.specificOwnerNotebookFactory = new SpecificOwnerNotebookComponentFactory();
    this.specificCollaborativeNotebookFactory = new SpecificCollaborativeNotebookComponentFactory();
    this.mainComponents = {
      "createNotebookForm": $(".js_create_notebook_wrapper"),
      "myNotebookArea": $(".js_myNotebook_area"),
      "coNotebookArea": $(".js_coNotebook_area"),
      "specificOwnerNotebook": $(".js_specific_owner_notebook_area"),
      "specificCollaboratorNotebook": $(".js_specific_collaborative_notebook_area"),
      "invitationsForm": $(".js_invitation_wrapper"),
      "settingForm": $(".js_user_profile_wrapper"),
    }
  }

  async init() {
    this.eventRegister();
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
        const specificOwnerNotebook = await this.getNotebook(displayComponent["path"]);
        await this.renderSpecificOwnerNotebook(specificOwnerNotebook);
        localStorage.setItem("lastMainComponent", displayComponentName);
        localStorage.setItem("lastOpenNotebookId", displayComponent["notebookId"]);
        break;
      case "specificCollaboratorNotebook":
        const specificCollaboratorNotebook = await this.getNotebook(displayComponent["path"]);
        await this.renderSpecificCollaborativeNotebook(specificCollaboratorNotebook);
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

  /**
   *
   * @returns Object
   *  notebookName: LinkNote開發歷程,
   *  description: LinkNote開發的血汗史
   *  id: NB000000001
   *  notes: []
   *  nextPage: false
   */
  getNotebook = async (path) => {
    const response = await this.requestHandler.sendRequestWithToken(path, "GET", null);
    if(!response.ok) {
      this.messageSender.error("Get notes failed");
    }
    return await response.json();
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
      const data = await response.json();

      Swal.fire({
        title: 'Success!',
        text: 'Create notebook success!',
        icon: 'success',
        showConfirmButton: true,
        timer: 5000
      }).then(() => {
        this.clearCreateNotebookFormInput();
        //建立notebook後渲染該頁面
        this.displayMainComponent({
          displayComponentName: "specificOwnerNotebook",
          path: `/api/notebooks/${data.notebookId}/notes`,
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
    localStorage.getItem("lastOpen")
    // todo 取消後應該要渲染上一本開的notebook，這邊暫時先渲染myNotebook
    this.displayMainComponent({
      displayComponentName: "myNotebookArea",
      path: "/api/notebooks?offset=0&limit=20"
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
    const notebookContainer = this.ownerNotebookFactory.generateOwnerNotebooks(notebooks);
    myNotebookArea.append(notebookContainer);
  }

  renderCollaborativeNotebooks = (notebooks) => {
    const collaborativeNotebookArea = $(".js_coNotebook_area");
    const noneNotebooks = $(`.js_none_collaborative_notebook`);
    $(".js_collaborative_notebook_ctn").empty();

    if(notebooks.length === 0) {
      noneNotebooks.show();
      return;
    }
    $('.js_coNotebook_top').show();
    noneNotebooks.hide();
    const notebookContainer = this.collaborativeNotebookFactory.generateCollaborativeNotebooks(notebooks);
    collaborativeNotebookArea.append(notebookContainer);
  }

  renderSpecificOwnerNotebook = async (notebook) => {
    const ownerNotebookArea = $(".js_specific_owner_notebook_area");
    ownerNotebookArea.empty();
    const ownerNotebook = await this.specificOwnerNotebookFactory.generateSpecificOwnerNotebook(notebook);
    ownerNotebookArea.append(ownerNotebook);
  }

  renderSpecificCollaborativeNotebook = async (notebook) => {
    const collaborativeNotebookArea = $(".js_specific_collaborative_notebook_area");
    collaborativeNotebookArea.empty();
    const collaborativeNotebook = await this.specificCollaborativeNotebookFactory.generateSpecificCollaborativeNotebook(notebook);
    collaborativeNotebookArea.append(collaborativeNotebook);
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
      $('.js_received_header').removeClass("display-none");
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
      $('.js_sent_invitation_header').removeClass("display-none");
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

