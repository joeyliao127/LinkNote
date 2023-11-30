const apiUrl = "http://localhost:8080";
// const apiUrl = "https://linknote.online";
function baseInit() {
  const token = localStorage.getItem("token");
  if (token) {
    verifyUserToken(token);
  } else {
    window.location.href = "/";
  }
}

async function verifyUserToken(token) {
  const response = await fetch(apiUrl + "/api/user/auth", {
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
  }
}

async function fetchData(path, method, body) {
  console.log(`path = ${path}`);
  const response = await fetch(apiUrl + path, {
    headers: {
      "Content-Type": "application/json",
      Authorization: "Bearer " + token,
    },
    method: method,
    body: JSON.stringify(body),
  });
  return response.json();
}

function lazyLoading(fetchParams) {
  console.log(`執行lazy loading`);
  const notebooks = document.querySelectorAll(".myNotebook .notebook");
  const observer = new IntersectionObserver(async (entry) => {
    if (entry[0].isIntersecting) {
      observer.unobserve(entry[0].target);
      fetchData();
    }
  });
  //觀察最後一個notebook
  observer.observe(notebooks[notebooks.length - 1]);
}

function verifyEmailRegx(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}
baseInit();
