package com.github.terigina.voting.dao;

import com.github.terigina.voting.service.Color;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface IVotingDAO extends AutoCloseable {

    boolean voteFor(UUID uuid, Color color);

    int getVotes(Color color);

    Optional<Color> getVote(UUID userId);

    Map<Color, Integer> getVotes();
}
