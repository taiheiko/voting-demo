package com.github.terigina.voting.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static com.github.terigina.voting.service.Color.BLUE;
import static com.github.terigina.voting.service.Color.GREEN;
import static com.github.terigina.voting.service.Color.RED;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorTest {

    private static Stream<Arguments> colors() {
        return Stream.of(
                Arguments.of(Optional.of(RED), "RED"),
                Arguments.of(Optional.of(RED), "red"),
                Arguments.of(Optional.of(RED), " red"),
                Arguments.of(Optional.of(RED), "red "),
                Arguments.of(Optional.of(GREEN), "GREEN"),
                Arguments.of(Optional.of(GREEN), "GREEN "),
                Arguments.of(Optional.of(GREEN), " GREEN"),
                Arguments.of(Optional.of(GREEN), "Green"),
                Arguments.of(Optional.of(BLUE), "bLUE  "),
                Arguments.of(Optional.of(BLUE), "bLUE"),
                Arguments.of(Optional.of(BLUE), "blue"),
                Arguments.of(Optional.of(BLUE), " blue"),
                Arguments.of(Optional.empty(), ""),
                Arguments.of(Optional.empty(), "  "),
                Arguments.of(Optional.empty(), null),
                Arguments.of(Optional.empty(), "yellow")
        );
    }

    @ParameterizedTest
    @MethodSource("colors")
    void shouldReturnColorOf(Optional<Color> expected, String colorName) {
        assertEquals(expected, Color.of(colorName));
    }
}