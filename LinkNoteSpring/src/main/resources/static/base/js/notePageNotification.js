class MsgMaker {
  static msgCtn;
  static success(msg) {
    this.msgCtn = document.querySelector("#success");
    this.setMsg(msg);
  }

  static error(msg) {
    this.msgCtn = document.querySelector("#failed");

    this.setMsg(msg);
  }

  static warn(msg) {
    this.msgCtn = document.querySelector("#warn");
    this.setMsg(msg);
  }

  static setMsg(msg) {
    this.msgCtn.textContent = msg;
    this.msgCtn.classList.toggle("display-none");
    setTimeout(() => {
      this.msgCtn.classList.toggle("display-none");
    }, 3000);
  }
}
