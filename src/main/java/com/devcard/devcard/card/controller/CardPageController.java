package com.devcard.devcard.card.controller;

import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.service.CardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CardPageController {

    private final CardService cardService;

    public CardPageController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/cards/{id}/view")
    public String viewCard(@PathVariable Long id, Model model) {
        CardResponseDto card = cardService.getCard(id);
        model.addAttribute("card", card);
        return "cardDetail";
    }
}
