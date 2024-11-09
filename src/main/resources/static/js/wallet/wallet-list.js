function createNewGroup() {
    fetch('/groups', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ name: '새로운 그룹' })  // 새로운 그룹의 기본 이름
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            }
            throw new Error('Failed to create group');
        })
        .then(data => {
            location.reload(); // 페이지 새로고침으로 새 그룹 목록을 갱신
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

function redirectToGroup(groupId) {
    window.location.href = `/groups/${groupId}/cards`;  // 원하는 URL로 리다이렉트
}