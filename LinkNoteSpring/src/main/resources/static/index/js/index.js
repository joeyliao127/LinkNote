let errMsg = document.querySelector("#signin-error-msg");
async function init() {
  const token = localStorage.getItem("token");
  if (token) {
    verifyUserByToken(token);
    window.location.href = "/notebooks";
  }
  switchFormBtn();
  register();
  signInBtnListener();
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

function clearErrorMsg() {
  setTimeout(() => {
    document.querySelector("#signup-error-msg").textContent = "";
  }, 5000);
}

function validateEmailFormat(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

function register() {
  const signUpBtn = document.querySelector(".signup-ctn .form-ctn button");
  signUpBtn.addEventListener("click", async () => {
    const email = document.querySelector("#signup-email").value;
    const username = document.querySelector("#username").value;
    const password = document.querySelector("#signup-password").value;
    const confirmPassword = document.querySelector("#confirmPassword").value;
    if (!validateEmailFormat(email)) {
      document.querySelector("#signup-error-msg").textContent =
        "Please enter the correct format for your email. ";
      clearErrorMsg();
      return 0;
    }
    if (password != confirmPassword) {
      document.querySelector("#signup-error-msg").textContent =
        "Please ensure that your passwords match.";
      clearErrorMsg();
      return 0;
    }
    await fetchRegisterEndpoint(username, email, password);
  });
}

async function fetchRegisterEndpoint(username, email, password) {
  const endpoint = "/api/user/register";
  const requestBody = { username, email, password };
  const request = {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(requestBody),
  };

  const response = await fetch(endpoint, request);
  const data = await response.json();
  console.log(`token = ${data.token}`);
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

//return boolean
function validateEmailFormatAndPasswordNotNull(email, password) {
  console.log(`email: ${email}\npassowrd: ${password}`);
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email) && password;
}

function signInBtnListener() {
  const signinBtn = document.querySelector(".signin-ctn button");
  signinBtn.addEventListener("click", async () => {
    const email = document.querySelector("#signin-email").value;
    const password = document.querySelector("#signin-password").value;
    const endpoint = "/api/user/auth";
    const reqBody = { email: email, password: password };
    console.log(`email: ${email}, ps:${password}`);
    const checkInput = validateEmailFormatAndPasswordNotNull(email, password);
    if (!checkInput) {
      errMsg.textContent =
        "Please enter the correct format for your email and password can not be null ";
      return 0;
    }
    try {
      const response = await fetch(endpoint, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(reqBody),
      });
      const data = await response.json();
      console.log(data);
      if (data.result) {
        localStorage.setItem("token", data.token);
        window.location.href = "/notebooks";
      } else {
        errMsg.textContent = "Email or password incorrect. ";
      }
    } catch (e) {
      console.log(`網路或Server異常: ${e}`);
    }
  });
}

async function verifyUserByToken(token) {
  const response = await fetch("/api/user/auth", {
    headers: {
      Authorization: "Bearer " + token,
      "Content-Type": "application/json",
    },
    method: "POST",
  });
  const verifyResult = await response.json();
  console.log(`token驗證結果：`);
  console.log(verifyResult);
  if (!verifyResult.parseResult) {
    localStorage.removeItem("token");
    window.location.href = "/";
  }
}
init();
