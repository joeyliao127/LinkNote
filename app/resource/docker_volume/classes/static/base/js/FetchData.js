class FetchDataHandler {
  constructor() {}

  static setRequestHeader = function (method, requestBody) {
    if (requestBody) {
      return {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
        method: method,
        body: JSON.stringify(requestBody),
      };
    } else {
      return {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
        method: method,
      };
    }
  };

  static async fetchData(path, method, requestBody) {
    if (requestBody) {
      return await fetch(path, {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
        method: method,
        body: JSON.stringify(requestBody),
      });
    } else {
      return await fetch(path, {
        headers: {
          "Content-Type": "application/json",
          Authorization: "Bearer " + localStorage.getItem("token"),
        },
        method: method,
      });
    }
  }
}
