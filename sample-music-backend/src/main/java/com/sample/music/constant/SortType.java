package com.sample.music.constant;

import lombok.Getter;

@Getter
public enum SortType {
    NEWEST("releaseDate", "DESC"),
    OLDEST("releaseDate", "ASC"),
    LISTENERS_ASC("listeners", "ASC"),
    LISTENERS_DESC("listeners", "DESC"),
    LIKES_ASC("likes", "ASC"),
    LIKES_DESC("likes", "DESC");

    private final String field;
    private final String direction;

    SortType(String field, String direction) {
        this.field = field;
        this.direction = direction;
    }

}