package com.vasileva.entity;

import lombok.Getter;

@Getter
public enum Rating {
    G("G"),
    PG("PG"),
    PG13("PG-13"),
    R("R"),
    NC17("NC-17");

    private final String code;

    Rating(String code) {
        this.code = code;
    }

    public static Rating fromCode(String code) {
        for (Rating rating : values()) {
            if (rating.code.equals(code)) {
                return rating;
            }
        }
        throw new IllegalArgumentException("Invalid rating code: " + code);
    }
}
