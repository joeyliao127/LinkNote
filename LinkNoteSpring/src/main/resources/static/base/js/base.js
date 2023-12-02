const apiUrl = "http://localhost:8080";
// const apiUrl = "https://linknote.online";
const token = localStorage.getItem("token");
function baseInit() {
  if (token) {
    verifyUserToken(token);
  } else {
    window.location.href = "/";
  }
}
let i = 0;
async function verifyUserToken(token) {
  i += 1;
  console.log("執行token", i);
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
    location.href = "/";
  }
}

async function fetchData(path, method, body) {
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

// function lazyLoading(fetchParams) {
//   console.log(`執行lazy loading`);
//   const notebooks = document.querySelectorAll(".myNotebook .notebook");
//   const observer = new IntersectionObserver(async (entry) => {
//     if (entry[0].isIntersecting) {
//       observer.unobserve(entry[0].target);
//     }
//   });
//   //觀察最後一個notebook
//   observer.observe(notebooks[notebooks.length - 1]);
// }

function verifyEmailRegx(email) {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}
baseInit();
