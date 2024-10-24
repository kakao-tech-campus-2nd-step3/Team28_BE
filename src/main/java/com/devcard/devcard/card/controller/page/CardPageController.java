package com.devcard.devcard.card.controller.page;

import com.devcard.devcard.card.dto.CardResponseDto;
import com.devcard.devcard.card.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class CardPageController {

    private final CardService cardService;

    @Autowired
    private Environment env;

    public CardPageController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping("/cards/{id}/view")
    public String viewCard(@PathVariable Long id, Model model) {
        CardResponseDto card = cardService.getCard(id);
        model.addAttribute("card", card);
        String kakaoJavascriptKey = env.getProperty("kakao.javascript.key");
        return "cardDetail";
    }
}
