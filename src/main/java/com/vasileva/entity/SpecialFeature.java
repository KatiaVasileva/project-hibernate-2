package com.vasileva.entity;

import lombok.Getter;

@Getter
public enum SpecialFeature {
    TRAILERS("Trailers"),
    COMMENTARIES("Commentaries"),
    DELETED_SCENES("Deleted Scenes"),
    BEHIND_THE_SCENES("Behind the Scenes");

    private final String code;

    SpecialFeature(String code) {
        this.code = code;
    }
}
