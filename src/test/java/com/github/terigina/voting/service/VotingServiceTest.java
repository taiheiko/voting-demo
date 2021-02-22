package com.github.terigina.voting.service;

import com.github.terigina.voting.dao.IVotingDAO;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static com.github.terigina.voting.service.Color.BLUE;
import static com.github.terigina.voting.service.Color.GREEN;
import static com.github.terigina.voting.service.Color.RED;
import static java.util.Collections.emptyMap;
import static java.util.Map.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasEntry;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VotingServiceTest {
    private IVotingDAO daoMock = mock(IVotingDAO.class);
    private VotingService service = new VotingService(daoMock);

    @Test
    void shouldReturnOptionsWithZeros() {
        when(daoMock.getVotes()).thenReturn(emptyMap());
        Map<Color, Integer> results = service.getResults();
        assertThat(results, allOf(hasEntry(RED, 0), hasEntry(GREEN, 0), hasEntry(BLUE, 0)));
    }

    @Test
    void shouldReturnOptionsWithValues() {
        when(daoMock.getVotes()).thenReturn(of(GREEN, 2, RED, 5, BLUE, 3));
        Map<Color, Integer> results = service.getResults();
        assertThat(results, allOf(hasEntry(RED, 5), hasEntry(GREEN, 2), hasEntry(BLUE, 3)));
    }

    @Test
    void shouldReturnOptionsWithPartialValues() {
        when(daoMock.getVotes()).thenReturn(of(GREEN, 4));
        Map<Color, Integer> results = service.getResults();
        assertThat(results, allOf(hasEntry(RED, 0), hasEntry(GREEN, 4), hasEntry(BLUE, 0)));
    }

    @Test
    void itShouldCloseDao() throws Exception {
        service.close();
        verify(daoMock).close();
    }
}