const token = localStorage.getItem("token");
function baseInit() {
  if (token) {
    verifyUserToken(token);
  } else {
    window.location.href = "/";
  }
}

async function verifyUserToken(token) {
  const response = await fetch("/api/user/auth", {
    headers: {
      Authorization: "Bearer " + token,
      "Content-Type": "application/json",
    },
    method: "POST",
  });
  const verifyResult = await response.json();
  if (!verifyResult) {
    localStorage.removeItem("token");
    location.href = "/";
  }
}

async function fetchData(path, method, body) {
  const response = await fetch(path, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    method: method,
    body: JSON.stringify(body),
  });
  if (response.status === 401) {
    window.location.href = "/notebooks";
  }
  return response.json();
}

function verifyEmailRegx(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}
baseInit();
