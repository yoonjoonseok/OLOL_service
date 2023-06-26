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

function getNotificationPermission() {
    // 브라우저 지원 여부 체크
    if (!("Notification" in window)) {
        alert("데스크톱 알림을 지원하지 않는 브라우저입니다.");
    }
    // 데스크탑 알림 권한 요청
    Notification.requestPermission(function (result) {
        // 권한 거절
        if (result == 'denied') {
            alert('알림을 차단하셨습니다.\n브라우저의 사이트 설정에서 변경하실 수 있습니다.');
            return false;
        }
    });
}