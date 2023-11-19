const apiUrl = "http://localhost:8080";
// const apiUrl = "https://linknote.online";

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
    window.location.href = "/";
  }
}

function baseInit() {
  const token = localStorage.getItem("token");
  if (token) {
    verifyUserToken(token);
  }
}

baseInit();
