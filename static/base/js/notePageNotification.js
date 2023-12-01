class MsgMaker {
  showMsg(type, msg) {
    let msgCtn;
    switch(type){
        case "success":
            msgCtn = document.querySelector("#success");
        case "failed":
            msgCtn = document.querySelector("#failed");
        case "warn":
            msgCtn = document.querySelector("#join");
        case "left":
            msgCtn = document.querySelector("#left");
        case "unlock":
            msgCtn = document.querySelector("#unlock");
        case ""
    }
   
    msgCtn.textContent = msg;
    msgCtn.classList.toggle("display-none");
    setTimeout(() => {
      msgCtn.classList.toggle("display-none");
    }, 3000);
  }
}

const show = new MsgMaker();
