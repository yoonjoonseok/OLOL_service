self.addEventListener("push", (event) => {
    const payload = JSON.parse(event.data.text());
    event.waitUntil(
        registration.showNotification(payload.notification.title, {
            body: payload.notification.body,
            image: payload.notification.image
        })
    );
});

self.addEventListener('notificationclick', function (event) {
    event.notification.close(); // 알림 닫기

    var url = event.notification.image; // 링크 URL 가져오기

    event.waitUntil(
        clients.openWindow(url) // 클릭 시 링크 열기
    );
});