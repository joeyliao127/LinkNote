class MsgMaker {
  static success(msg) {
    this.setMsg(msg, "success");
  }

  static error(msg) {
    this.setMsg(msg, "error");
  }

  static warn(msg) {
    this.setMsg(msg, "warn");
  }

  static setMsg(msg, type) {
    const wrapperNotification = document.querySelector(".wrapper-notification");
    const msgCtn = document.createElement("div");
    msgCtn.classList.add("notice");
    msgCtn.textContent = msg;
    msgCtn.setAttribute("id", type);
    wrapperNotification.appendChild(msgCtn);
    setTimeout(() => {
      msgCtn.remove();
    }, 3000);
  }
}
