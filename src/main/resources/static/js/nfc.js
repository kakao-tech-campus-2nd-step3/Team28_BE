async function sendCardViaNFC() {
    const ndef = new NDEFReader();

    // NFC 읽기 시작
    async function startScanning() {
        // 이벤트 핸들러 연결
        ndef.onreading = event => {
            console.log('NFC tag read:', event);
            alert('NFC tag successfully read!');

            // NDEF 태그에 데이터 작성
            try {
                const url = "https://naver.com";

                ndef.write({
                    records: [
                        {
                            recordType: "url",
                            data: url
                        }
                    ]
                })

                alert('NFC tag successfully write!');
                console.log('Data written to NEF tag')
            } catch (error) {
                alert('Error writing to NFC tag: ' + error.message);
            }
        };

        // 오류 핸들러
        ndef.onreadingerror = event => {
            alert('NFC reading error occurred.');
            console.error('NFC reading error occurred. Event data: ', event);
        };

        try {
            await ndef.scan();  // NFC 스캔 시작
            alert('NFC 스캔 시작...');
            console.log('NFC scan started successfully.');
        } catch (error) {
            alert(`Error starting NFC scan: ${error.message}`);
            console.error('Error starting NFC scan:', error);
        }
    }

    // 권한 확인 및 처리
    const nfcPermissionStatus = await navigator.permissions.query({name: "nfc"});

    if (nfcPermissionStatus.state === "granted") {
        // 권한 이미 부여된 경우
        alert('NFC permission granted, starting scan...');
        startScanning();
    } else if (nfcPermissionStatus.state === "denied") {
        // 권한 거부된 경우
        alert('NFC permission was denied. Please enable it in your browser settings.');
    } else {
        document.querySelector("#scanButton").style.display = "block";
        document.querySelector("#scanButton").onclick = event => {
            startScanning();
        };
    }
}

async function readNFCAndOpenURL() {
    if ('NDEFReader' in window) {
        const ndef = new NDEFReader();
        const decoder = new TextDecoder();

        try {
            ndef.scan(); // NFC 스캔 시작
            alert('NFC 스캔 시작...(reading)');

            ndef.onreading = event => {
                alert("onreading 이벤트 시작")
                for (const record of event.message.records) {
                    alert("record " + record.data);
                    if (record.recordType === "url") {
                        const url = decoder.decode(record.data);
                        alert('URL read from NFC tag: ' + url);
                        window.location.href = url;
                    }
                }
            };
            // 오류 핸들러
            ndef.onreadingerror = event => {
                alert('NFC reading error occurred.(reading) ');

                alert('Error type: ' + event.type); // Error type: reading error

                if (event.error) {
                    alert('Error details: ' + event.error.message);
                    console.error('Error details: ', event.error);
                } else {
                    alert('No detailed error information available.');
                }

                console.error('NFC reading error occurred. Event data: ');
            };

        } catch (error) {
            alert('Error reading from NFC tag: ' + error.message);
            console.error('Error reading from NFC tag:', error);
        }
    } else {
        alert('NDEFReader is not supported on this device.');
    }
}
