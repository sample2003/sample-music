package com.sample.music.constant;

import lombok.Getter;

@Getter
public enum TargetType {
    ALL("all"),
    TITLE("title"),
    ARTIST("artist"),
    ALBUM("album"),
    STYLE("style"),
    TAG("tag");

    private final String type;
    TargetType(final String type) {
        this.type = type;
    }
}
