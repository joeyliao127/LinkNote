class MsgMaker {
  static success = "success";
  static error = "error";
  static warn = "warn";

  static showMsg(type, msg) {
    let msgCtn;
    switch (type) {
      case "success":
        console.log(`綠色`);
        msgCtn = document.querySelector("#success");
        break;
      case "error":
        console.log(`紅色`);
        msgCtn = document.querySelector("#failed");
        break;
      case "warn":
        console.log(`橘色`);
        msgCtn = document.querySelector("#warn");
        break;
    }

    msgCtn.textContent = msg;
    msgCtn.classList.toggle("display-none");
    setTimeout(() => {
      msgCtn.classList.toggle("display-none");
    }, 3000);
  }
}
