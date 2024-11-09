package com.devcard.devcard.card.controller.page;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.auth.model.OauthMemberDetails;
import com.devcard.devcard.card.dto.GroupResponseDto;
import com.devcard.devcard.card.entity.Group;
import com.devcard.devcard.card.service.GroupService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class WalletListController {

    private final GroupService groupService;

    public WalletListController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("wallet-list")
    public String walletList(Model model, @AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {
        Member member = oauthMemberDetails.getMember();
        List<GroupResponseDto> groups = groupService.getGroupsByMember(member);
        model.addAttribute("groups", groups);

        return "wallet-list";
    }
}
