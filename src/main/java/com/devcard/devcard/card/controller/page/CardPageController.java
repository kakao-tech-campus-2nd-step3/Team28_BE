package com.devcard.devcard.card.controller.page;

import com.devcard.devcard.auth.model.OauthMemberDetails;
import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.service.CardService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class CardPageController {

    private final CardService cardService;

    @Value("${kakao.javascript.key}")
    private String kakaoJavascriptKey;

    public CardPageController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/cards/{id}/view")
    public String viewCard(@PathVariable("id") Long id, Model model) {
        CardResponseDto card = cardService.getCard(id);
        model.addAttribute("card", card);
        model.addAttribute("kakaoJavascriptKey", kakaoJavascriptKey);
        return "card-detail";
    }

    @GetMapping("/cards/{id}/update")
    public String updateCard(@PathVariable("id") Long id, Model model) {
        CardResponseDto card = cardService.getCard(id);
        model.addAttribute("card", card);
        return "card-update";
    }

    @GetMapping("/groups/{id}/cards")
    public String viewCardList(@PathVariable("id") Long groupId, Model model, @AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {
        List<CardResponseDto> cards = cardService.getCardsByGroup(groupId, oauthMemberDetails.getMember());
        model.addAttribute("cards", cards);
        return "card-list";
    }

}
