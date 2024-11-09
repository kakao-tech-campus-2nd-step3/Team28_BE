package com.devcard.devcard.card.controller.rest;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.model.OauthMemberDetails;
import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.dto.CardRequestDto;
import com.devcard.devcard.card.service.CardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @PostMapping
    public ResponseEntity<CardResponseDto> createCard(
            @Valid @RequestBody CardRequestDto cardRequestDto,
            @AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {

        Member member = oauthMemberDetails.getMember();
        CardResponseDto responseDto = cardService.createCard(cardRequestDto, member);
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

    @GetMapping()
    public ResponseEntity<List<CardResponseDto>> getCards() {
        List<CardResponseDto> responseDto = cardService.getCards();
        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CardResponseDto> updateCard(
            @PathVariable Long id,
            @Valid @RequestBody CardRequestDto cardRequestDto,
            @AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {

        Member member = oauthMemberDetails.getMember();
        CardResponseDto responseDto = cardService.updateCard(id, cardRequestDto, member);
        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable Long id,
            @AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {

        Member member = oauthMemberDetails.getMember();
        cardService.deleteCard(id, member);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/add-to-group/{groupId}")
    public ResponseEntity<Void> addCardToGroup(
            @PathVariable Long id,
            @PathVariable Long groupId
    ) {
        cardService.addCardToGroup(id, groupId);

        return ResponseEntity.ok().build();
    }

}
