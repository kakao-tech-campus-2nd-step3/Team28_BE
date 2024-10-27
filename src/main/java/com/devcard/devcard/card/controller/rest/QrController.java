package com.devcard.devcard.card.controller.rest;

import com.devcard.devcard.card.dto.QrResponseDto;
import com.devcard.devcard.card.service.QrServiceImpl;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class QrController {

    private final QrServiceImpl qrServiceImpl;

    @Autowired
    public QrController(QrServiceImpl qrServiceImpl) {
        this.qrServiceImpl = qrServiceImpl;
    }

    @GetMapping("/cards/{card_id}/qrcode")
    public ResponseEntity<QrResponseDto> createQR(@PathVariable (name = "card_id") Long cardId) throws IOException, WriterException {
        String qrUrl = qrServiceImpl.createQr(cardId);

        return ResponseEntity.ok()
                .body(new QrResponseDto(qrUrl));
    }
}
