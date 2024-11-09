package com.devcard.devcard.card.dto;

public class GroupResponseDto {
    private Long id;
    private String name;
    private int count;

    public GroupResponseDto(Long id, String name, int count) {
        this.id = id;
        this.name = name;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
