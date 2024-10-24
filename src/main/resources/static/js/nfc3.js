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

async function sendCardViaNFC() {

    const ndef = new NDEFReader();

    async function startScanning() {
        ndef.onreading = event => {
            console.log('NFC tag read:', event);
            alert('onReading');
        };

        ndef.onreadingerror = event => {
            alert('NFC reading error: ' + event.error);
            console.error('NFC reading error: ', event.error);
        };

        try {
            await ndef.scan();
        } catch (error) {
            alert(`Error: ${error.message}`);
        }
    }


    const nfcPermissionStatus = await navigator.permissions.query({ name: "nfc" });
    if (nfcPermissionStatus.state === "granted") {
        // NFC access was previously granted, so we can start NFC scanning now.
        alert('d');
        startScanning();
        alert('ds');

    } else {
        // Show a "scan" button.
        document.querySelector("#scanButton").style.display = "block";
        document.querySelector("#scanButton").onclick = event => {
            // Prompt user to allow UA to send and receive info when they tap NFC devices.
            startScanning();
        };
    }

    // const ndef = new NDEFReader();
    // alert("0");
    // ndef
    //     .scan()
    //     .then(() => {
    //         alert("1");
    //
    //         ndef.onreading = (event) => {
    //             console.log("NDEF message read.");
    //             alert("ㅁㅊㅁㅊ");
    //         };
    //         alert("2");
    //
    //         console.log("Scan started successfully.");
    //         ndef.onreadingerror = (event) => {
    //             console.log(
    //                 "Error! Cannot read data from the NFC tag. Try a different one?",
    //             );
    //             alert("ㅜㅜ");
    //         };
    //         alert("3");
    //
    //     })
    //     .catch((error) => {
    //         console.log(`Error! Scan failed to start: ${error}.`);
    //     });


    // ndef.write("태그 적기 성공")
    //
    // // 1. addEventListener 방식
    // ndef.addEventListener('reading', (event) => {
    //     alert("NFC 태그 스캔 성공! (addEventListener)" + event);
    //     // 이 부분에서 .추가 로직을 처리
    // });
    //
    // // 2. onreading 방식
    // ndef.onreading = (event) => {
    //     alert("NFC 태그 스캔 성공! (onreading)"+event);
    //     // 이 부분에서 추가 로직을 처리
    // };
    //
    // // 태그 스캔 실패 이벤트
    // ndef.addEventListener('readingerror', (event) => {
    //     alert("NFC 태그 스캔 실패. 다시 시도해 주세요." + event);
    // });
    // ndef.onreading= (event) => {
    //     alert("성공");
    // }
    // ndef.onreadingerror =  (event) => {
    //     alert("에러발생 ");
    // }


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
