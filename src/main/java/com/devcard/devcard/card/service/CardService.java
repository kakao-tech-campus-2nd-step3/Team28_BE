package com.devcard.devcard.card.service;

import com.devcard.devcard.card.dto.CardRequestDto;
import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.exception.CardNotFoundException;
import com.devcard.devcard.card.repository.CardRepository;
import com.devcard.devcard.card.entity.Card;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public CardResponseDto createCard(CardRequestDto cardRequestDto) {
        Card card = new Card.Builder()
                .name(cardRequestDto.getName())
                .company(cardRequestDto.getCompany())
                .position(cardRequestDto.getPosition())
                .email(cardRequestDto.getEmail())
                .phone(cardRequestDto.getPhone())
                .build();

        Card savedCard = cardRepository.save(card);
        return CardResponseDto.fromEntity(savedCard);
    }

    @Transactional(readOnly = true)
    public CardResponseDto getCard(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("해당 명함을 찾을 수 없습니다."));
        return CardResponseDto.fromEntity(card);
    }

    @Transactional
    public CardResponseDto updateCard(Long id, CardRequestDto cardRequestDto) {
        Card existingCard = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("해당 명함을 찾을 수 없습니다."));

        existingCard.updateFromDto(cardRequestDto);
        cardRepository.save(existingCard);
        return CardResponseDto.fromEntity(existingCard);
    }

    @Transactional
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("해당 명함을 찾을 수 없습니다."));
        cardRepository.delete(card);
    }


}
