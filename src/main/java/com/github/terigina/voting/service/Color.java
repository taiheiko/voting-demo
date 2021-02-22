package com.github.terigina.voting.service;

import java.util.Arrays;
import java.util.Optional;

public enum Color {
    RED,
    GREEN,
    BLUE;

    public static Optional<Color> of(String color) {
        String upperColor = String.valueOf(color).trim().toUpperCase();
        return Arrays.stream(Color.values()).
                filter(c -> c.name().equals(upperColor))
                .findFirst();
    }

}
