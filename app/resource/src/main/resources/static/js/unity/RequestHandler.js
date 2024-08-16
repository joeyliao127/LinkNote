export class RequestHandler {

  async sendRequestWithToken(path, method, requestBody) {
    switch (method) {
      case "GET":
        return await this.sendGetRequest(path);
      case "POST":
        return await this.sendPostRequest(path, requestBody);
      case "PUT":
        return await this.sendPutRequest(path, requestBody);
      case "DELETE":
        return await this.sendDeleteRequest(path);
      default:
        return null;
    }
  }

  async sendGetRequest(path) {
    return await fetch(path, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
      method: "GET",
    });
  }

  async sendPostRequest(path, requestBody) {
    return await fetch(path, {
      headers: {
        "Content-Type": "application/json",
         Authorization: "Bearer " + localStorage.getItem("token"),
      },
      method: "POST",
      body: JSON.stringify(requestBody),
    });
  }

  async sendPutRequest(path, requestBody) {
    return await fetch(path, {
      headers: {
        "Content-Type": "application/json",
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
      method: "PUT",
      body: JSON.stringify(requestBody),
    });
  }

  async sendDeleteRequest(path) {
    return await fetch(path, {
      headers: {
        Authorization: "Bearer " + localStorage.getItem("token"),
      },
      method: "DELETE",
    });
  }
}