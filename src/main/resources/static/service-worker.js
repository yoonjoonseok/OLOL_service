var eventSource = new EventSource('/notification/subscribe');

eventSource.onmessage = function (event) {
    const message = event.data;
    //getNotificationPermission();
    //new Notification(message);
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

self.addEventListener('close', () => {
    if (eventSource) {
        eventSource.close();
    }
    console.log("이벤트소스 클로즈");
});