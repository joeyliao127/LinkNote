function init() {
  switchForm();
}

function switchForm() {
  const toSigninBtn = document.querySelector("#toSignin");
  const toSignupBtn = document.querySelector("#toSignup");
  toSigninBtn.addEventListener("click", () => {
    const signinCtn = document.querySelector(".signin-ctn");
    const signupCtn = document.querySelector(".signup-ctn");
    signinCtn.classList.toggle("display-none");
    signupCtn.classList.toggle("display-none");
  });
  toSignupBtn.addEventListener("click", () => {
    const signinCtn = document.querySelector(".signin-ctn");
    const signupCtn = document.querySelector(".signup-ctn");
    signinCtn.classList.toggle("display-none");
    signupCtn.classList.toggle("display-none");
  });
}

init();
