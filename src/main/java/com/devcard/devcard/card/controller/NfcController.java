package com.devcard.devcard.card.controller;

import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.exception.CardNotFoundException;
import com.devcard.devcard.card.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NfcController {

    private final CardService cardService;

    public NfcController(CardService cardService) {
        this.cardService = cardService;
    }

    /**
     *
     * @param cardId 명함 ID
     * @return 명함 정보를 담은 JSON 객체
     */
    @GetMapping("/cards/{card_id}/nfc")
    public ResponseEntity<CardResponseDto> getMyCardData(@PathVariable(value = "card_id") Long cardId) {
        try {
            CardResponseDto cardResponseDto = cardService.getCard(cardId);
            return ResponseEntity.ok(cardResponseDto);
        } catch (CardNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
