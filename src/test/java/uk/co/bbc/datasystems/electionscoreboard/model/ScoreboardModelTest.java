package uk.co.bbc.datasystems.electionscoreboard.model;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ScoreboardModelTest {
    private ScoreboardModel scoreboardModel;

    @Before
    public void setUp() {
        scoreboardModel = new ScoreboardModel();
    }

    @Test
    public void checkPartyIsPresent() {
        assertEquals(false, scoreboardModel.checkPartyIsPresent("TEST"));

        scoreboardModel.addParty("TEST");
        assertEquals(true, scoreboardModel.checkPartyIsPresent("TEST"));
    }

    @Test
    public void addPartyIfNotPresent() {
        assertEquals(0, scoreboardModel.getParties().size());

        scoreboardModel.addParty("TEST");
        assertEquals(1, scoreboardModel.getParties().size());

        scoreboardModel.addParty("TEST");
        assertEquals(1, scoreboardModel.getParties().size());
    }

    @Test
    public void getSortedPartyList() {
        scoreboardModel.addParty("P1");
        scoreboardModel.addParty("P2");
        scoreboardModel.addParty("P3");

        // P1 has 2 seats
        scoreboardModel.getParties().get("P1").winSeat();
        scoreboardModel.getParties().get("P1").winSeat();

        // P2 has 1 seat
        scoreboardModel.getParties().get("P2").winSeat();

        // P3 has 3 seats
        scoreboardModel.getParties().get("P3").winSeat();
        scoreboardModel.getParties().get("P3").winSeat();
        scoreboardModel.getParties().get("P3").winSeat();

        List<Party> sortedPartyList = scoreboardModel.getSortedPartyList();
        assertEquals("P3", sortedPartyList.get(0).getPartyCode());
        assertEquals("P1", sortedPartyList.get(1).getPartyCode());
        assertEquals("P2", sortedPartyList.get(2).getPartyCode());
    }

    @Test
    public void updateVotesForParty() {
        scoreboardModel.addParty("TEST");
        assertEquals(0, scoreboardModel.getParties().get("TEST").getVotes());

        scoreboardModel.updateVotesForParty("TEST", 1234);
        assertEquals(1234, scoreboardModel.getParties().get("TEST").getVotes());

        scoreboardModel.updateVotesForParty("TEST", 1234);
        assertEquals(2468, scoreboardModel.getParties().get("TEST").getVotes());
    }

    @Test
    public void incrementSeatsForParty() {
        scoreboardModel.addParty("TEST");
        assertEquals(0, scoreboardModel.getParties().get("TEST").getSeats());

        scoreboardModel.incrementSeatsForParty("TEST");
        assertEquals(1, scoreboardModel.getParties().get("TEST").getSeats());

        scoreboardModel.incrementSeatsForParty("TEST");
        assertEquals(2, scoreboardModel.getParties().get("TEST").getSeats());
    }
}
