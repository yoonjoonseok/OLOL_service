var eventSource = new EventSource('/notification/subscribe');

eventSource.onmessage = function (event) {
    const message = event.data;
    console.log(message);

    const title = message;
    const options = {};

    event.waitUntil(self.registration.showNotification(title, options));

    console.log("성공");
};

eventSource.onerror = function (event) {
    // 에러 처리 로직
    console.log("실패");
}