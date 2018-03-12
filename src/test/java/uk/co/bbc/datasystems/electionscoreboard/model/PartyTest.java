package uk.co.bbc.datasystems.electionscoreboard.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PartyTest {
    private Party party;

    @Before
    public void setUp() {
        party = new Party("TEST");
    }

    @Test
    public void updateVotes() {
        party.updateVotes(1234);
        assertEquals(1234, party.getVotes());

        party.updateVotes(1234);
        assertEquals(2468, party.getVotes());
    }

    @Test
    public void winSeat() {
        party.winSeat();
        assertEquals(1, party.getSeats());

        party.winSeat();
        assertEquals(2, party.getSeats());
    }
}
