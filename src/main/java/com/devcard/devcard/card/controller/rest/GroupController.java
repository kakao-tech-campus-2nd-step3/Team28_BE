package com.devcard.devcard.card.controller.rest;

import com.devcard.devcard.auth.model.OauthMemberDetails;
import com.devcard.devcard.card.dto.GroupResponseDto;
import com.devcard.devcard.card.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/groups")
public class GroupController {

    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<GroupResponseDto> createGroup(@AuthenticationPrincipal OauthMemberDetails oauthMemberDetails) {
        GroupResponseDto group = groupService.createGroup("새로운 그룹", oauthMemberDetails.getMember());
        return ResponseEntity.ok(group);
    }

}
