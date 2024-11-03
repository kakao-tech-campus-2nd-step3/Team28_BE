$(document).ready(function() {
    $(".nav-item").removeClass("active"); // 모든 nav-item에서 active 클래스 제거
    $(".nav-item[data-page='chats']").addClass("active"); // chats에 active 클래스 추가
});