class AuthenticationHandler {
  constructor() {
    this.token = localStorage.getItem("token");
    console.log(`this.token = ${this.token}`);
    if (this.token) {
      this.verifyUserToken();
    } else {
      window.location.href = "/";
      console.log(`沒有token`);
    }
  }

  static async verifyUserToken() {
    const path = "/api/auth/user/token";

    const response = await FetchDataHandler.fetchData(path, "POST");

    const verifyResult = await response.json();

    if (!verifyResult) {
      localStorage.removeItem("token");
      location.href = "/";
    }
    return true;
  }

  static validateEmailFormat(email) {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  }
}
