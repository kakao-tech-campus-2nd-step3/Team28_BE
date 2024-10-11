package com.devcard.devcard.card.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class QrServiceImpl implements QrService{

    private static final int QR_SIZE_WIDTH = 200;
    private static final int QR_SIZE_HEIGHT = 200;

    @Value("${qr.domain.uri}")
    private String domainUri;

    @Value("${qr.code.directory}")
    private String qrCodeDirectory;

    /**
     * @param cardId QR로 만들 명함 ID
     * @return QR 코드 IMAGE 파일 이름만 반환
     */
    @Override
    public String createQr(Long cardId) throws WriterException, IOException {

        // QR URL - QR 코드 정보 URL
        String url = generateQrUrl(cardId);

        // QR Code - BitMatrix: qr code 정보 생성
        BitMatrix bitMatrix = generateQrCode(url);

        // Setting QR Image File Name, Path
        String qrFileName = generateQrFileName(cardId);
        Path qrPath = generateQrFilePath(qrFileName);

        // Save QR
        saveQrCodeImage(bitMatrix, qrPath);

        return domainUri + "qrcodes/" + qrFileName;
    }

    private String generateQrUrl(Long cardId) {
        return domainUri + "cards/" + cardId;
    }

    private BitMatrix generateQrCode(String url) throws WriterException {
        try {
            return new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, QR_SIZE_WIDTH, QR_SIZE_HEIGHT);
        } catch (WriterException e) {
            throw e;
        }
    }

    private String generateQrFileName(Long cardId) {
        return "card_id_" + cardId + ".png";
    }

    private Path generateQrFilePath(String qrFileName) {
        return Paths.get(qrCodeDirectory + qrFileName);
    }

    private void saveQrCodeImage(BitMatrix bitMatrix, Path qrPath) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            MatrixToImageWriter.writeToStream(bitMatrix, "png", out);
            Files.createDirectories(qrPath.getParent());
            Files.write(qrPath, out.toByteArray());
        } catch (IOException e) {
            throw e;
        }
    }
}
