let errMsg = document.querySelector("#signin-error-msg");
async function init() {
  const token = localStorage.getItem("token");
  if (token) {
    window.location.href = "/userspace.html";
  }
  switchFormBtn();
  register();
  signin();
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
  const endpoint = apiUrl + "/api/user";
  console.log(endpoint);
  console.log(`username:`, username);
  console.log(`email:`, email);
  console.log(`password:`, password);
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
    window.location.href = "/userspace.html";
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
function validateEmailFormat(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

async function signin() {
  const email = document.querySelector("#signin-email");
  const password = document.querySelector("#signin-password");
  const signinBtn = document.querySelector(".signin-ctn button");

  signinBtn.addEventListener("click", async () => {
    const token = await verfityUsernameAndPassword(email.value, password.value)
      .token;
    console.log(token);
    localStorage.setItem("token", token);
    window.location.href = "/userSpace.html";
  });
}

async function verfityUsernameAndPassword(email, password) {
  const endpoint = apiUrl + "/api/user/auth";
  const reqBody = { email: email, password: password };

  try {
    const response = await fetch(endpoint, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(reqBody),
    });
    const data = await response.json();
    if (response.ok) {
      return data;
    } else {
      throw new Error(data.msg);
    }
  } catch (e) {
    errMsg.textContent = e.message;
    console.log(e);
    return 0;
  }
}

//return boolean
async function verifyUserToken(token) {}

init();
