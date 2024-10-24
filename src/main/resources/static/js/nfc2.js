// 로그인 후 명함 데이터를 백엔드에서 가져오기
async function getMyCardData(cardId) {
    try {
        const response = await fetch(`/cards/${cardId}/nfc`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const cardData = await response.json();
            return cardData;  // 사용자 명함 데이터 반환
        } else {
            const errorMessage = await response.text();
            alert(`Failed to retrieve card data: ${errorMessage}`);
            return null;
        }
    } catch (error) {
        console.error("Error fetching card data:", error);
        alert("An error occurred while fetching card data.");
        return null;
    }
}

// NFC 요청
async function sendCardViaNFC() {
    const cardId = 2;  // 임시로 고정된 값, 동적으로 변경 필요
    const cardData = await getMyCardData(cardId);  // 명함 데이터 가져오기

    if (!cardData) {
        alert("Could not retrieve card data.");
        return;
    }

    try {
        if (!('NDEFReader' in window)) {
            alert("This device does not support NFC Reading.");
            return;
        }

        const ndef = new NDEFReader();

        // NFC 태그 감지 대기
        await ndef.scan();
        // 여기서 NFC 태그가 감지되었는지 확인
        alert("시작");
        ndef.onreading = async (event) => {
            // 이 부분이 실행되면 NFC 태그가 감지된 것
            try {
                // NFC 쓰기 시도
                await ndef.write("Hello World");
                alert("4");
                console.log("NFC Tag written with card data: Hello World");
                alert("Card data successfully written to NFC tag.");
            } catch (writeError) {
                console.error("Error writing to NFC tag: ", writeError);
                alert("An error occurred while writing to NFC tag: " + writeError);
            }
        };
        alert("끝");

    } catch (error) {
        console.error("Error during NFC scan: ", error);
        alert("An error occurred while scanning NFC tags: " + error);
    }

}

// NFC 데이터 수신 자동 시작
// async function autoReceiveCardViaNFC() {
//     try {
//         if (!('NDEFReader' in window)) {
//             alert("This device does not support NFC reading.");
//             return;
//         }
//
//         const reader = new NDEFReader();
//         await reader.scan(); // NFC 스캔 시작
//
//         reader.onreading = (event) => {
//             const ndefMessage = event.message;
//             for (const record of ndefMessage.records) {
//                 const textDecoder = new TextDecoder(record.encoding);
//                 const receivedCardData = textDecoder.decode(record.data); // JSON 형식의 명함 데이터 수신
//
//                 try {
//                     // 수신한 JSON을 파싱
//                     const cardData = JSON.parse(receivedCardData);
//
//                     // 수신된 명함 데이터를 alert로 알림
//                     alert(`Received card data:\nName: ${cardData.name}\nCompany: ${cardData.company}\nPosition: ${cardData.position}\nPhone: ${cardData.phone}\nEmail: ${cardData.email}`);
//
//                     displayCard(cardData);  // 명함 정보 화면에 표시
//                 } catch (error) {
//                     console.error("Error parsing NFC data: ", error);
//                     alert("An error occurred while parsing NFC data.");
//                 }
//             }
//         };
//     } catch (error) {
//         console.error("Error reading NFC tag: ", error);
//         alert("An error occurred while reading the NFC tag."+ error);
//     }
// }

// NFC 데이터 수신 자동 시작
// async function autoReceiveCardViaNFC() {
//     try {
//         if (!('NDEFReader' in window)) {
//             alert("This device does not support NFC reading.");
//             return;
//         }
//
//         const reader = new NDEFReader();
//         await reader.scan(); // NFC 스캔 시작
//
//         reader.onreading = (event) => {
//             const ndefMessage = event.message;
//             for (const record of ndefMessage.records) {
//                 const textDecoder = new TextDecoder(record.encoding);
//                 const receivedCardData = textDecoder.decode(record.data); // JSON 형식의 명함 데이터 수신
//
//                 // 수신된 데이터 확인
//                 alert(`Received data: ${receivedCardData}`);
//
//                 if (receivedCardData === "Hello world") {
//                     alert("Success! NFC has received the message.");
//                 } else {
//                     alert("Received unexpected data.");
//                 }
//
//                 // 수신한 JSON을 파싱하려는 부분 (필요시 사용)
//                 try {
//                     const cardData = JSON.parse(receivedCardData);
//                     displayCard(cardData);  // 명함 정보 화면에 표시
//                 } catch (error) {
//                     console.error("Error parsing NFC data: ", error);
//                     alert("An error occurred while parsing NFC data.");
//                 }
//             }
//         };
//     } catch (error) {
//         console.error("Error reading NFC tag: ", error);
//         alert("An error occurred while reading the NFC tag." + error);
//     }
// }

// 수신한 명함 데이터를 화면에 표시
function displayCard(cardData) {
    const cardDisplay = document.getElementById("card-display");
    cardDisplay.innerHTML = `
        <div class="card">
            <h2>${cardData.name}</h2>
            <p>Company: ${cardData.company}</p>
            <p>Position: ${cardData.position}</p>
            <p>Phone: ${cardData.phone}</p>
            <p>Email: ${cardData.email}</p>
        </div>
    `;
}
