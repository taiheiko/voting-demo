package com.github.terigina.voting.service;

import com.github.terigina.voting.dao.H2VotingDAO;
import com.github.terigina.voting.dao.IVotingDAO;
import com.github.terigina.voting.model.responses.GetOptionsResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
        return new GetOptionsResponse(votingDAO.getVote(userId));
    }

    public boolean vote(UUID userId, String color) {
        return Color.of(color)
                .filter(value -> votingDAO.voteFor(userId, value))
                .isPresent();
    }

    public Map<Color, Integer> getResults() {
        Map<Color, Integer> votes = new HashMap<>(votingDAO.getVotes());
        Arrays.stream(Color.values())
                .forEach(c -> votes.putIfAbsent(c, 0));
        return votes;
    }

    @Override
    public void close() throws Exception {
        votingDAO.close();
    }
}
