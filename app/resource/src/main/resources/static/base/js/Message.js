class MessageMaker {
  constructor() {}

  static #genMessageBlock(message) {
    const messageCtn = document.createElement("div");
    messageCtn.classList.add("messageCtn");
    messageCtn.classList.add("displayMessage");
    const status = document.createElement("div");
    status.classList.add("status");
    const msg = document.createElement("p");
    msg.textContent = message;
    const closeBtn = document.createElement("div");
    closeBtn.id = "close";

    closeBtn.addEventListener("click", () => {
      messageCtn.classList.remove("displayMessage");
      setTimeout(() => {
        messageCtn.remove();
      }, 2000);
    });
    messageCtn.appendChild(status);
    messageCtn.appendChild(msg);
    messageCtn.appendChild(closeBtn);
    return messageCtn;
  }

  static success(message) {
    const messageBlock = this.#genMessageBlock(message);
    messageBlock.classList.add("success");
    this.#appnedMessageBlock(messageBlock);
  }

  static failed(message) {
    const messageBlock = this.#genMessageBlock(message);
    messageBlock.classList.add("failed");
    this.#appnedMessageBlock(messageBlock);
  }

  static warning(message) {
    const messageBlock = this.#genMessageBlock(message);
    messageBlock.classList.add("warning");
    this.#appnedMessageBlock(messageBlock);
  }

  static #appnedMessageBlock(messageBlock) {
    setTimeout(() => {
      messageBlock.classList.remove("displayMessage");
      setTimeout(() => {
        messageBlock.remove();
      }, 2000);
    }, 5000);

    document.querySelector("body").appendChild(messageBlock);
  }
}
