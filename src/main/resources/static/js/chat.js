$(document).ready(function () {
    $(".nav-item").removeClass("active"); // 모든 nav-item에서 active 클래스 제거
    $(".nav-item[data-page='chats']").addClass("active"); // chats에 active 클래스 추가

    const path = window.location.pathname;

    // 특정 사용자 정보를 가져오는 함수
    function fetchUserProfile(participantId, callback) {
        $.ajax({
            url: `/api/chats/user/${participantId}/profile`, // 해당 사용자 ID로 프로필 요청
            method: "GET",
            success: function (profile) {
                callback(profile);
            },
            error: function (error) {
                console.error("프로필 정보를 불러오는데 오류가 발생했습니다:", error);
            }
        });
    }

    if (path === '/chats') {  // 채팅 목록 페이지
        let chatRoomsData = [];  // 원본 데이터 저장

        // 특정 유저의 참여 채팅방 목록을 가져오는 함수
        function fetchUserChatRooms() {
            $.ajax({
                url: `/api/chats/user/${memberId}`, // 본인 ID로 채팅방 목록 요청
                method: "GET",
                success: function (chatRooms) {
                    // lastMessageTime을 기준으로 오름차순(오래된 대화부터)으로 정렬
                    chatRooms.sort((a, b) => new Date(b.lastMessageTime) - new Date(a.lastMessageTime));
                    chatRoomsData = chatRooms;  // 검색을 위해 원본 데이터 저장
                    renderChatRooms(chatRooms);
                },
                error: function (error) {
                    console.error("채팅방 목록을 불러오는데 오류가 발생했습니다:", error);
                }
            });
        }

        // 채팅방 목록을 화면에 렌더링하는 함수
        function renderChatRooms(chatRooms, searchTerm = '') {
            const chatListContainer = $("#chat-list");
            chatListContainer.empty(); // 기존 목록 초기화

            const chatRoomPromises = chatRooms.map(room => {
                const participantId = room.participantsId.find(id => id !== memberId);
                const lastMessage = room.lastMessage || '';

                return new Promise((resolve) => {
                    fetchUserProfile(participantId, function(profile) {
                        // 검색어가 포함된 경우 하이라이트 처리
                        const highlightedName = highlightText(profile.nickname, searchTerm);
                        const highlightedMessage = highlightText(lastMessage, searchTerm);

                        const chatItem = $('<div>', {class: 'chat-item'});
                        chatItem.on('click', function () {
                            window.location.href = `/chats/${room.id.split("_")[1]}`;
                        });

                        const chatImage = $('<div>', {class: 'chat-image'}).append(
                            $('<img>', {
                                src: profile.profileImage,
                                alt: 'User Avatar'
                            })
                        );

                        const chatInfo = $('<div>', {class: 'chat-info'});
                        const chatName = $('<span>', {class: 'chat-name'}).html(highlightedName);
                        const chatMessage = $('<p>', {class: 'chat-message'}).html(highlightedMessage);
                        chatInfo.append(chatName).append(chatMessage);

                        const chatTime = $('<div>', {class: 'chat-time'}).append(
                            $('<span>', {text: formatChatTime(room.lastMessageTime)})
                        );

                        chatItem.append(chatImage).append(chatInfo).append(chatTime);

                        // 각 채팅 아이템을 resolve하여 완료
                        resolve(chatItem);
                    });
                });
            });

            // 모든 프로필 데이터가 준비된 후 한 번에 렌더링
            Promise.all(chatRoomPromises).then(chatItems => {
                chatItems.forEach(chatItem => chatListContainer.append(chatItem));
            });
        }

        // 하이라이트 함수
        function highlightText(text, searchTerm) {
            if (!searchTerm) return text;
            const regex = new RegExp(`(${searchTerm})`, 'gi');
            return text.replace(regex, '<span class="highlight">$1</span>');
        }

        // 검색창 이벤트 처리
        let debounceTimeout;
        $('#search-input').on('input', function() {
            clearTimeout(debounceTimeout);
            debounceTimeout = setTimeout(() => {
                const searchTerm = $(this).val().trim().toLowerCase();
                const filteredRooms = chatRoomsData.filter(room => {
                    const participants = room.participants.join(', ').toLowerCase();
                    const lastMessage = room.lastMessage ? room.lastMessage.toLowerCase() : '';
                    return participants.includes(searchTerm) || lastMessage.includes(searchTerm);
                });
                renderChatRooms(filteredRooms, searchTerm);  // 필터링된 채팅방 렌더링
            }, 300); // 300ms 딜레이 적용
        });


        // 날짜 및 시간 포맷팅 함수
        function formatChatTime(chatTimeText) {
            const chatTime = new Date(chatTimeText);
            const today = new Date();

            const isToday =
                chatTime.getFullYear() === today.getFullYear() &&
                chatTime.getMonth() === today.getMonth() &&
                chatTime.getDate() === today.getDate();

            if (isToday) {
                // 오늘인 경우 24시 형태의 시간만 표시
                return chatTime.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', hour12: false });
            } else if (chatTime.getFullYear() === today.getFullYear()) {
                // 올해일 경우 날짜만 표시 (11월 5일 형태)
                return `${chatTime.getMonth() + 1}월 ${chatTime.getDate()}일`;
            } else {
                // 올해가 아닐 경우 (YYYY.MM.DD.) 형태로 표시
                return `${chatTime.getFullYear()}.${chatTime.getMonth() + 1}.${chatTime.getDate()}.`;
            }
        }

        // 페이지 로드 시 본인 이름을 가져온 후 채팅방 목록 가져오기
        fetchUserChatRooms();
    } else if (path.startsWith('/chats/')) {  // 채팅 방 페이지
        const senderName = "나";  // 메시지를 보낸 사용자 이름 (적절한 값으로 수정)
        let receiverName = null;  // 상대방 이름 저장할 변수

        // 뒤로가기 아이콘 클릭 시 /chats로 이동
        const backButton = document.getElementById("backButton");
        backButton.addEventListener("click", function() {
            window.location.href = "/chats"; // /chats 페이지로 이동
        });

        const chatId = path.split('/')[2];
        fetchChatRoom(chatId);

        // 임시로 로컬 서버 설정
        const socket = new WebSocket(`ws://localhost:8080/ws?chatId=${chatId}&userId=${memberId}`);

        // 웹소켓 연결
        socket.addEventListener("open", () => {
            console.log("웹소켓 연결 성공");
        });

        // 웹소켓 예외처리
        socket.addEventListener("error", (error) => {
            console.error("웹소켓 연결 중 오류가 발생:", error);
        });

        // 새로고침 하거나 나갈 때 연결 끊기
        $(window).on('beforeunload', function () {
            if (socket) {
                socket.close();
            }
        });

        // 메시지 보내기
        function sendMessage(messageContent) {
            // 메시지 길이 체크
            if (messageContent.length > 2000) {
                console.log("메시지 최대 길이 초과");
                return;
            }

            const message = {
                senderId: memberId,  // 예시: 메시지를 보낸 사용자의 ID (적절한 값으로 수정)
                sender: senderName,
                content: messageContent  // 메시지 내용
            };

            socket.send(JSON.stringify(message));  // 서버로 메시지 보내기
            console.log("보낸 메시지:", messageContent);

            // 메시지 박스 추가
            const chatRoomContainer = $('#chatWindow');
            makeMessageBox(message, chatRoomContainer);

            // 자동 스크롤
            chatRoomContainer.scrollTop(chatRoomContainer[0].scrollHeight);
        }


        // 버튼 클릭 시 메시지 보내기
        const sendButton = document.getElementById("sendButton");
        sendButton.addEventListener('click', function() {
            const messageInput = document.getElementById("messageInput");
            const messageContent = messageInput.value;
            if (messageContent.trim()) { // 비어있는 메시지는 보내지 않음
                sendMessage(messageContent);
                messageInput.value = '';  // 메시지 전송 후 입력창 비우기
            }
        });

        // Enter 키로 메시지 보내기
        const messageInput = document.getElementById("messageInput");
        messageInput.addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault();  // 기본 Enter 동작 (줄바꿈)을 방지
                const messageContent = messageInput.value;
                if (messageContent.trim()) { // 비어있는 메시지는 보내지 않음
                    sendMessage(messageContent);
                    messageInput.value = '';  // 메시지 전송 후 입력창 비우기
                }
            }
        });


        // 특정 채팅방의 세부 정보를 가져오는 함수
        function fetchChatRoom(chatId) {
            $.ajax({
                url: `/api/chats/${chatId}`, // 특정 채팅방 ID로 요청
                method: "GET",
                success: function (chatRoom) {
                    renderChatRoom(chatRoom);
                },
                error: function (error) {
                    console.error("채팅방 정보를 불러오는데 오류가 발생했습니다:", error);
                }
            });
        }

        // 채팅방 세부 정보를 화면에 렌더링하는 함수
        function renderChatRoom(chatRoom) {
            const chatRoomContainer = $("#chatWindow");
            chatRoomContainer.empty(); // 기존 내용 초기화

            const participantId = parseInt(chatRoom.participantsId.find(id => id !== memberId));

            if (!receiverName) {
                // 프로필 닉네임을 비동기로 가져오고 receiverName 저장
                fetchUserProfile(participantId, function(profile) {
                    receiverName = profile.nickname;
                    renderMessages(chatRoom.messages);
                    $("#roomTitle").text(receiverName);
                });
            } else {
                renderMessages(chatRoom.messages);
            }
        }

        // 채팅 메시지를 렌더링하는 함수
        function renderMessages(messages) {
            const chatRoomContainer = $("#chatWindow");
            messages.forEach(message => {
                message.senderId = parseInt(message.sender.split("_")[1]);
                message.sender = (memberId === message.senderId) ? senderName : receiverName;
                makeMessageBox(message, chatRoomContainer);
            });
        }

        // 메세지 박스 만들기 로직
        function makeMessageBox(message, chatRoomContainer) {
            // 메시지 요소 생성
            // memberId면 오른쪽 / 상대면 왼쪽
            const messageElement = $('<div>', { class: message.senderId === memberId ? 'rightMessage right' : 'leftMessage left' });
            console.log("--- sender:" + message.senderId + " / member:" + memberId + " / " + (message.senderId === memberId));
            //  memberId와 같으면 sender 아니면 customer
            const senderNameClass = message.senderId === memberId ? 'sender' : 'customer';
            // 이름 표시
            const senderName = $('<div>', { class: senderNameClass, text: message.sender });
            // 메세지 표시
            const messageContent = $('<div>', { class: 'message-content' }).append($('<div>', { class: 'message-text', text: message.content }));

            // 메시지 구성
            messageElement.append(senderName).append(messageContent);

            // 채팅방에 메시지 추가
            chatRoomContainer.append(messageElement);
        }

    }
});
