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

function init() {
  const token = localStorage.getItem("token");
  if (token) {
    verifyUserToken(token);
  } else {
    window.location.href = "/";
  }
}
init();
