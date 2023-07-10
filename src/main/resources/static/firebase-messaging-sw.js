importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-app.js');
importScripts('https://www.gstatic.com/firebasejs/5.9.2/firebase-messaging.js');

firebase.initializeApp({
    messagingSenderId: "706361316814"
});

const messaging = firebase.messaging();

self.addEventListener('notificationclick', function (event) {
    event.notification.close(); // 알림 닫기

    var url = event.notification.image; // 링크 URL 가져오기

    event.waitUntil(
        clients.openWindow(url) // 클릭 시 링크 열기
    );
});