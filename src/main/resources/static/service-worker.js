var eventSource = new EventSource('/notification/subscribe');

eventSource.onmessage = function (event) {
    const message = JSON.parse(event.data);

    const title = message.title;
    const options = {
        body: message.body,
        data: {
            url: message.link
        }
    };

    event.waitUntil(self.registration.showNotification(title, options));

    console.log("성공");
};

eventSource.onerror = function (event) {
    // 에러 처리 로직
    console.log("실패");
};

self.addEventListener('notificationclick', function (event) {
    event.notification.close(); // 알림 닫기

    var url = event.notification.data.url; // 링크 URL 가져오기

    event.waitUntil(
        clients.openWindow(url) // 클릭 시 링크 열기
    );
});