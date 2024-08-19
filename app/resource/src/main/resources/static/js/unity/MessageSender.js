import iziToast from "izitoast";
import 'izitoast/dist/css/iziToast.min.css';

export class MessageSender {
  success (message, position="topRight") {
    iziToast.success({
      "title": "Success",
      "message": message,
      "position": position
    });
  }

  error (message, position="topRight") {
    iziToast.error({
      "title": "Error",
      "message": message,
      "position": position
    });
  }

  warning (message, position="topRight") {
    iziToast.warning({
      "title": "Warning",
      "message": message,
      "position": position
    });
  }

  info (message, position="topRight") {
    iziToast.info({
      "title": "Info",
      "message": message,
      "position": position
    });
  }
}