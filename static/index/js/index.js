async function init() {
  switchForm();
  submitRegisterForm();
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

function submitRegisterForm() {
  const signUpBtn = document.querySelector(".signup-ctn .form-ctn button");
  signUpBtn.addEventListener("click", async () => {
    const email = document.querySelector("#signup-email").value;
    const username = document.querySelector("#username").value;
    const password = document.querySelector("#signup-password").value;
    const confirmPassword = document.querySelector("#confirmPassword").value;
    if (!validateEmail(email)) {
      document.querySelector("#signup-error-msg").textContent =
        "Please enter the correct format for your email. ";
      return 0;
    }
    if (password != confirmPassword) {
      document.querySelector("#signup-error-msg").textContent =
        "Please ensure that your passwords match.";
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
  console.log(`data :`, data);
  if (response.status == 200) {
    window.alert("Success!");
    window.location.href = "/";
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
function validateEmail(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}
init();
