package com.github.taya_y.voting.dao;

import java.util.UUID;

public interface IVotingDAO extends AutoCloseable {

    boolean voteFor(UUID uuid, String color);

    int getVotes(String color);
}
