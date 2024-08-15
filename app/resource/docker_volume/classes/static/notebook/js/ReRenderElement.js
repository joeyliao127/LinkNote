class ReRenderElement {
  constructor() {}

  static reRenderMain(element) {
    const mainWrapper = document.querySelector(".mainWrapper");
    mainWrapper.innerHTML = "";
    mainWrapper.appendChild(element);
  }
}
