package com.github.terigina.voting.model.responses;

import com.github.terigina.voting.service.Color;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

public class GetOptionsResponse {

    private final Collection<Color> options = Arrays.asList(Color.values());
    private final Optional<Color> choice;

    public Collection<Color> getOptions() {
        return options;
    }

    public GetOptionsResponse(Optional<Color> choice) {
        this.choice = choice;
    }

    public Color getChoice() {
        return choice.orElse(null);
    }
}
