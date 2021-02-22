package com.github.terigina.voting.dao;

import com.github.terigina.voting.service.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
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
        int votes = votingDao.getVotes(Color.RED);
        votingDao.voteFor(userId, Color.RED);
        assertEquals(votes + 1, votingDao.getVotes(Color.RED));
    }

    @Test
    void shouldReVote() {
        UUID userId = UUID.randomUUID();
        votingDao.voteFor(userId, Color.RED);
        int votesRed = votingDao.getVotes(Color.RED);
        int votesGreen = votingDao.getVotes(Color.GREEN);
        votingDao.voteFor(userId, Color.GREEN);
        assertEquals(votesRed - 1, votingDao.getVotes(Color.RED), "Red votes should have decremented");
        assertEquals(votesGreen + 1, votingDao.getVotes(Color.GREEN), "green votes should have incremented");
    }

    @Test
    void shouldGetVotes() {
        Map<Color, Integer> votesBefore = votingDao.getVotes();
        votingDao.voteFor(UUID.randomUUID(), Color.GREEN);
        Map<Color, Integer> votesAfter = votingDao.getVotes();
        assertEquals(votesBefore.getOrDefault(Color.RED, 0), votesAfter.getOrDefault(Color.RED, 0));
        assertEquals(votesBefore.get(Color.GREEN) + 1, votesAfter.get(Color.GREEN));
        assertEquals(votesBefore.getOrDefault(Color.BLUE, 0), votesAfter.getOrDefault(Color.BLUE, 0));
    }
}