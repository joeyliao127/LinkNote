function init() {
  noteCardLitener();
}

function noteCardLitener() {
  const noteCardList = document.querySelectorAll(".main-item-noteCard");
  noteCardList.forEach((card) => {
    card.addEventListener("click", () => {
      window.location.href = "/notebooks/1/notes/1";
    });
  });
}

function createNote() {}
init();
