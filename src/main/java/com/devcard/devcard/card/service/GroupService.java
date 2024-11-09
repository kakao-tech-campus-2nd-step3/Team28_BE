package com.devcard.devcard.card.service;

import com.devcard.devcard.auth.entity.Member;
import com.devcard.devcard.card.dto.GroupResponseDto;
import com.devcard.devcard.card.entity.Group;
import com.devcard.devcard.card.repository.GroupRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Transactional
    public GroupResponseDto createGroup(String name, Member member) {
        Group group = new Group(name, member);
        group = groupRepository.save(group);

        return new GroupResponseDto(group.getId(), group.getName(), group.getCount());
    }

    public List<GroupResponseDto> getGroupsByMember(Member member) {
        List<Group> groups = groupRepository.findByMember(member);

        return groups.stream()
                .map(group -> new GroupResponseDto(group.getId(), group.getName(), group.getCount()))
                .collect(Collectors.toList());
    }
}
