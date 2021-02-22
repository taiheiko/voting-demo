package com.github.terigina.voting.service;

import com.github.terigina.voting.dao.H2VotingDAO;
import com.github.terigina.voting.dao.IVotingDAO;
import com.github.terigina.voting.model.responses.GetOptionsResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class VotingService implements AutoCloseable {

    private final IVotingDAO votingDAO;

    public VotingService() {
        this(new H2VotingDAO());
    }

    public VotingService(IVotingDAO votingDAO) {
        this.votingDAO = votingDAO;
    }

    public GetOptionsResponse getOptions(UUID userId) {
        Optional<Color> choice = votingDAO.getVote(userId);
        return new GetOptionsResponse(choice);
    }

    public void vote(UUID userId, String color) {
        Color.of(color).ifPresent(value -> votingDAO.voteFor(userId, value));
    }

    public Map<Color, Integer> getResults() {
        Map<Color, Integer> votes = new HashMap<>(votingDAO.getVotes());
        for (Color color : Color.values()) {
            votes.putIfAbsent(color, 0);
        }
        return votes;
    }

    @Override
    public void close() throws Exception {
        votingDAO.close();
    }
}
