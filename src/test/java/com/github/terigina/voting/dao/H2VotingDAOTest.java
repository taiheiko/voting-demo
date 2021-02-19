package com.github.terigina.voting.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class H2VotingDAOTest {

    private IVotingDAO votingDao = new H2VotingDAO();

    private static Stream<Arguments> votes() {
        return Stream.of(
                Arguments.of(UUID.randomUUID(), "red"),
                Arguments.of(UUID.randomUUID(), "green"),
                Arguments.of(UUID.randomUUID(), "blue")
        );
    }

    @ParameterizedTest
    @MethodSource("votes")
    void shouldVote(UUID userId, String color) {
        int votes = votingDao.getVotes(color);
        votingDao.voteFor(userId, color);
        assertEquals(votes + 1, votingDao.getVotes(color));
    }

    @Test
    void shouldReVote() {
        UUID userId = UUID.randomUUID();
        final String red = "red";
        final String green = "green";
        votingDao.voteFor(userId, red);
        int votesRed = votingDao.getVotes(red);
        int votesGreen = votingDao.getVotes(green);
        votingDao.voteFor(userId, green);
        assertEquals(votesRed - 1, votingDao.getVotes(red), "Red votes should have decremented");
        assertEquals(votesGreen + 1, votingDao.getVotes(green), "green votes should have incremented");
    }
}