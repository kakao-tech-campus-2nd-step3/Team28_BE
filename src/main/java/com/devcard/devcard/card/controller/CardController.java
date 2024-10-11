package com.devcard.devcard.card.controller;

import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.entity.Card;
import com.devcard.devcard.card.dto.CardRequestDto;
import com.devcard.devcard.card.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(@Valid @RequestBody CardRequestDto cardRequestDto) {
        CardResponseDto responseDto = cardService.createCard(cardRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.getId())
                .toUri();
        return ResponseEntity.created(location).body(responseDto);
    }


    @GetMapping("/{id}")
    public ResponseEntity<CardResponseDto> getCard(@PathVariable Long id) {
        CardResponseDto responseDto = cardService.getCard(id);
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponseDto> updateCard(@PathVariable Long id, @Valid @RequestBody CardRequestDto cardRequestDto) {
        CardResponseDto responseDto = cardService.updateCard(id, cardRequestDto);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long id) {
        cardService.deleteCard(id);
        return ResponseEntity.noContent().build();
    }


}
