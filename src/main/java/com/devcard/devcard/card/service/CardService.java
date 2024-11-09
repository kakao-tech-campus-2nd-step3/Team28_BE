package com.devcard.devcard.card.service;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.card.dto.CardRequestDto;
import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.entity.Group;
import com.devcard.devcard.card.exception.CardNotFoundException;
import com.devcard.devcard.card.repository.CardRepository;
import com.devcard.devcard.card.entity.Card;
import com.devcard.devcard.card.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final GroupRepository groupRepository;

    public CardService(CardRepository cardRepository, GroupRepository groupRepository) {
        this.cardRepository = cardRepository;
        this.groupRepository = groupRepository;
    }

    @Transactional
    public CardResponseDto createCard(CardRequestDto cardRequestDto, Member member) {
        Card card = new Card.Builder(member)
                .company(cardRequestDto.getCompany())
                .position(cardRequestDto.getPosition())
                .phone(cardRequestDto.getPhone())
                .bio(cardRequestDto.getBio())
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

    @Transactional(readOnly = true)
    public List<CardResponseDto> getCards() {
        List<Card> cards = cardRepository.findAll();
        return cards.stream()
                .map(CardResponseDto::fromEntity)
                .toList();
    }

    @Transactional
    public CardResponseDto updateCard(Long id, CardRequestDto cardRequestDto, Member member) {
        Card existingCard = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("해당 명함을 찾을 수 없습니다."));

        // 명함 소유자 확인
        if (!existingCard.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("권한이 없습니다.");
        }

        // 기존 엔티티의 필드 업데이트
        existingCard.updateFromDto(cardRequestDto);

        return CardResponseDto.fromEntity(existingCard);
    }

    @Transactional
    public void deleteCard(Long id, Member member) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException("해당 명함을 찾을 수 없습니다."));

        // 명함 소유자 확인
        if (!card.getMember().getId().equals(member.getId())) {
            throw new RuntimeException("권한이 없습니다.");
        }

        cardRepository.delete(card);
    }

    @Transactional
    public void addCardToGroup(Long cardId, Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException("해당 명함을 찾을 수 없습니다."));

        group.addCard(card);
        groupRepository.save(group);
    }

    @Transactional(readOnly = true)
    public List<CardResponseDto> getCardsByGroup(Long groupId, Member member) {
        Group group = groupRepository.findByIdAndMember(groupId, member)
                .orElseThrow(() -> new RuntimeException("해당 그룹을 찾을 수 없습니다."));

        return group.getCards().stream()
                .map(CardResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

}
