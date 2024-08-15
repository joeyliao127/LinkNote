function main() {
  init();
  switchFormBtn();
  register();
  signInBtnListener();
}

async function init() {
  if (localStorage.getItem("token")) {
    const result = await AuthenticationHandler.verifyUserToken();
    if (result) {
      location.href = "/notebooks";
    }
  }
}

function switchFormBtn() {
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

function register() {
  const signUpBtn = document.querySelector(".signup-ctn .form-ctn button");
  signUpBtn.addEventListener("click", signupBtnCallback);
  async function signupBtnCallback() {
    const email = document.querySelector("#signup-email").value;
    const username = document.querySelector("#username").value;
    const password = document.querySelector("#signup-password").value;
    const confirmPassword = document.querySelector("#confirmPassword").value;
    if (!AuthenticationHandler.validateEmailFormat(email)) {
      errorMsgHandler("Please enter the correct format for your email. ");
      return 0;
    }
    if (password != confirmPassword) {
      errorMsgHandler("Please ensure that your passwords match.");
      return 0;
    }
    await fetchRegisterEndpoint(username, email, password);
  }

  async function fetchRegisterEndpoint(username, email, password) {
    const requestPath = "/api/user/register";
    const requestBody = { username, email, password };
    const response = await FetchDataHandler.fetchData(
      requestPath,
      "POST",
      requestBody
    );
    const data = await response.json();

    if (response.status == 201) {
      localStorage.setItem("token", data.token);
      window.alert("Success!");
      window.location.href = "/notebooks";
    } else if (response.status == 400) {
      document.querySelector("#signup-error-msg").textContent =
        "Email already exist.";
      console.log(`Error msg: ${data.message}`);
    } else {
      document.querySelector("#signup-error-msg").textContent =
        "Email already exist.";
      console.log(`Error msg: ${data.message}`);
    }
  }
}

function signInBtnListener() {
  const signinBtn = document.querySelector(".signin-ctn button");
  document
    .querySelector("#signin-email")
    .addEventListener("keypress", async (e) => {
      if (e.key === "Enter") {
        signinBtnCallback();
      }
    });

  document
    .querySelector("#signin-password")
    .addEventListener("keypress", async (e) => {
      if (e.key === "Enter") {
        signinBtnCallback();
      }
    });
  signinBtn.addEventListener("click", signinBtnCallback);

  async function signinBtnCallback() {
    const email = document.querySelector("#signin-email").value;
    const password = document.querySelector("#signin-password").value;
    const requestBody = { email: email, password: password };
    const checkInput = validateEmailFormatAndPasswordNotNull(email, password);

    if (!checkInput) {
      errorMsgHandler(
        "Please enter the correct format for your email and password can not be null "
      );
    }

    //開發環境path
    // const path = "http://127.0.0.1/api/auth/user/signin";
    //正式環境path
    const path = "/api/auth/user/signin";
    const response = await fetch(path, {
      headers: {
        "Content-Type": "application/json",
      },
      method: "POST",
      body: JSON.stringify(requestBody),
    });

    const data = await response.json();
    if (data.result) {
      localStorage.setItem("token", data.token);
      window.location.href = "/notebooks";
    } else {
      errorMsgHandler("Email or password incorrect. ");
    }
  }
}

//return boolean
function validateEmailFormatAndPasswordNotNull(email, password) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email) && password;
}

function errorMsgHandler(message) {
  let errMsg = document.querySelector("#signin-error-msg");
  errMsg.textContent = message;
  setTimeout(() => {
    document.querySelector("#signup-error-msg").textContent = "";
  }, 5000);
}

main();
